/*
* JTerm - a cross-platform terminal
* Copyright (code) 2017 Sergix, NCSGeek
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package main.java.jterm.command;

import main.java.jterm.JTerm;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Class for the "Clear" command
 *
 * Clears the terminal display by removing all lines
 */
public class Clear
{

    final private String ANSI_CLS = "\u001b[2J";
    final private String ANSI_HOME = "\u001b[H";

    public Clear(ArrayList<String> options)
    {

        for (String option : options)
        {
          if (option.equals("-h"))
          {
              System.out.println("Command syntax:\n\tclear [-h]\n\nClears all lines in the terminal display.");
            
          }

        }

        // If '-h' was not used - Clear the Screen
        if (!options.contains("-h"))
        {
            if (JTerm.isUnix)
            {
                // Use escape sequences to clear the screen for Unix OS
                System.out.print(ANSI_CLS + ANSI_HOME);
                System.out.flush();
            }
            else if (JTerm.isWin)
            {
                // Invoke the command line interpreter's own 'clear' command for Windows OS
                try
                {
                  new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                }
                catch (IOException | InterruptedException e)
                {
                  System.out.println(e);
                }
            }
        }
    }
}
