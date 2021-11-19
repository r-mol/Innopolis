import java.io.*;
import java.util.*;
import java.lang.*;

class FileIsEmptyException extends Exception {
}

class LongFileException extends Exception {
}

class WidthNotSpecifiedException extends Exception {
}

class InvalidWidthException extends Exception {
}

class EmptyWordException extends Exception {
}

class ForbiddenSymbolException extends Exception {
    ForbiddenSymbolException(Character e) {
        super(e.toString());
    }
}

class LimitIsExceededException extends Exception {
    LimitIsExceededException(String e) {
        super(e);
    }
}

class LimitIsExceededMaxWidthException extends Exception {
    LimitIsExceededMaxWidthException(String e) {
        super(e);
    }
}

public class TextJustification2 {
    public static void main(String[] args) {
        File inputfile = new File("input.txt");
        String text = null;
        int maxWidth = 0;
        String except = null;
        Character[] symbols = {' ', '.', ',', '!', '?', '-', ':', ';', '(', ')', '\'', '"'};
        ArrayList<Character> symbolsList = new ArrayList<Character>(Arrays.asList(symbols));
        boolean found = true;

        try {
            Scanner scanner = new Scanner(inputfile);

            if (scanner.hasNextLine()) {
                text = scanner.nextLine();
            } else {
                throw new FileIsEmptyException();
            }
            if (text.length() > 300) {
                throw new LongFileException();
            }
            if (scanner.hasNextInt()) {
                maxWidth = scanner.nextInt();
            } else {
                throw new WidthNotSpecifiedException();
            }
            if (maxWidth <= 0) {
                throw new InvalidWidthException();
            }
            if (text.charAt(0) == ' ') {
                throw new EmptyWordException();
            }

            int begin = 0;
            int cc = 1;
            boolean check = false;
            for (int i = 0; i < text.length(); i++) {
                if (text.charAt(i) == ' ' && text.charAt(i + 1) == ' ') {
                    throw new EmptyWordException();
                }
                if ((text.charAt(i) < 65 || text.charAt(i) > 90) && (text.charAt(i) < 97 || text.charAt(i) > 122) && !symbolsList.contains(text.charAt(i))) {
                    throw new ForbiddenSymbolException(text.charAt(i));
                }
                if (text.charAt(i) == ' ' || i == text.length() - 1 || i == 0) {
                    cc++;
                    if (!check && begin == 0) {
                        check = true;
                    } else {
                        if ((i - begin - 1) > 20) {
                            String[] result = text.split(" ", cc);
                            throw new LimitIsExceededException(result[result.length - 1]);
                        }
                        if ((i - begin - 1) > maxWidth) {
                            String[] result = text.split(" ", cc);
                            throw new LimitIsExceededMaxWidthException(result[result.length - cc + 4]);
                        }
                        begin = i;
                        check = false;
                    }
                }
            }

            StringBuilder txt = new StringBuilder(text);
            int rows = 1;
            int[][] Spaces = new int[100][3];
            int countLast = 0;

            //Cut text on rows and calculate
            //how much additional spaces program
            //should add to text.
            for (int i = maxWidth; i < txt.length(); i += maxWidth - countLast ) {

                // -> End of row is a space
                // -> Delete space which divides two rows
                // -> Go to next row
                if (txt.charAt(i) == ' ') {
                    txt.deleteCharAt(i);
                    found = false;
                    countLast = 0;
                }
                // -> End of row is a char
                // -> Find first space from end
                // -> Calculate count of additional spaces
                // -> Delete space which divides two rows
                // -> Go to next row
                else if (txt.charAt(i) != ' ') {
                    int j = i;
                    while (txt.charAt(j) != ' ' && j != 0) {
                        j--;
                    }

                    int countNewSpacesLeft = i - j;
                    int count = 0;

                    txt.deleteCharAt(j);

                    // Calculate count of spaces
                    for (int q = i - maxWidth; q < i; q++) {
                        if (txt.charAt(q) == ' ') {
                            count++;
                        }
                    }

                    int y = 0;

                    if (count < 1) {
                        text = txt.toString();
                        text = text.substring(0, j) + " " + text.substring(j, text.length());
                        txt = new StringBuilder(text);
                        count = 1;
                        y = 1;
                    }

                    countLast = countNewSpacesLeft;

                    // Write count of spaces for given row
                    if (count == 1 && y == 1) {
                        Spaces[rows][0] = countNewSpacesLeft;
                        Spaces[rows][1] = 0;
                        Spaces[rows][2] = count;
                    }else if(count == 1){
                        Spaces[rows][0] = countNewSpacesLeft+1;
                        Spaces[rows][1] = 0;
                        Spaces[rows][2] = count;
                    } else if ((count + countNewSpacesLeft) % count == 0) {
                        Spaces[rows][0] = (count + countNewSpacesLeft) / count;
                        Spaces[rows][1] = 0;
                        Spaces[rows][2] = count;
                    } else {
                        Spaces[rows][0] = (count + countNewSpacesLeft) / count + 1;
                        Spaces[rows][1] = (count + countNewSpacesLeft) / count;
                        Spaces[rows][2] = (count + countNewSpacesLeft) % count;
                    }
                }
                rows++;
            }
            text = txt.toString();

            // Add additional spaces to row
            int row = 1;
            while (row != rows) {
                for (int i = maxWidth * (row - 1); i < text.length(); i++) {
                    if (text.charAt(i) == ' ') {
                        if (Spaces[row][2] != 0) {
                            int temp = Spaces[row][0];
                            String firstpart = text.substring(0, i);
                            String lastpart = text.substring(i + 1, text.length());
                            while (temp != 0) {
                                firstpart += ' ';
                                temp--;
                            }

                            text = firstpart + lastpart;
                            Spaces[row][2]--;
                        }
                        else{
                            int temp = Spaces[row][1];
                            String firstpart = text.substring(0, i);
                            String lastpart = text.substring(i + 1, text.length());
                            while (temp != 0) {
                                firstpart += ' ';
                                temp--;
                            }
                        }
                        if(Spaces[row][0] != 0) {
                            i += Spaces[row][0] - 1;
                        }
                    }
                }
                row++;
            }
            found = false;
        } catch (FileNotFoundException e) {
            except = "Exception, file not found!";
        } catch (FileIsEmptyException fileIsEmpty) {
            except = "Exception, file is empty!";
        } catch (LongFileException e) {
            except = "Exception, input exceeds text max size!";
        } catch (WidthNotSpecifiedException widthNotSpecified) {
            except = "Exception, intended line width is not specified!";
        } catch (InvalidWidthException invalidWidth) {
            except = "Exception, line width cannot be negative or zero!";
        } catch (EmptyWordException e) {
            except = "Exception, input contains an empty word!";
        } catch (ForbiddenSymbolException e) {
            except = "Exception, input contains forbidden symbol '" + e.getMessage() + "'!";
        } catch (LimitIsExceededException e) {
            except = "Exception, '" + e.getMessage() + "' exceeds the limit of 20 symbols!";
        } catch (LimitIsExceededMaxWidthException e) {
            except = "Exception, '" + e.getMessage() + "' exceeds " + maxWidth + " symbols!";
        }


        // Output Exception/correct answer
        try {
            FileWriter writer = new FileWriter("output.txt");
            if (found) {
                writer.write(except);
            } else {
                for (int i = 0; i < text.length(); i++) {
                    if (i % maxWidth == 0 && i != 0) {
                        writer.write("\n");
                        writer.write(text.charAt(i));
                    } else {
                        writer.write(text.charAt(i));
                    }

                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}