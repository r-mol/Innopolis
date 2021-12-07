import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

class FileIsEmptyException extends Exception {
}
class InvalidLengthOperationException extends Exception {
}
class NonExistingOperation extends Exception {
}
class InvalidLengthNumberException extends Exception {
}
class DivisionByZeroException extends Exception {
}
class NegativeSqrtException extends Exception {
}
class InvalDataException extends Exception {}

public class Main {
    public static void main(String[] args) throws IOException {
        String except = null;
        File input = new File("input.txt");
        FileWriter writer = new FileWriter("output.txt");
        ArrayList<Integer> operations = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        String tt = "";
        ArrayList<Number> numbers = new ArrayList<>();
        ArrayList<Number> answer = new ArrayList<>();
        double divisor = 0;

        try {
            Scanner scanner = new Scanner(input);
            if (scanner.hasNextLine()) {
                temp.add(scanner.nextLine());
                for (int i = 0; i < temp.get(0).length(); i += 2) {
                    operations.add(temp.get(0).charAt(i) - 48);
                }
            } else {
                throw new FileIsEmptyException();
            }

            if (scanner.hasNextLine()) {
                temp.add(scanner.nextLine());
                for (int i = 0; i < temp.get(1).length(); i++) {
                    if (temp.get(1).charAt(i) != ' ' && temp.get(1).charAt(i) != temp.get(1).length() - 1) {
                        if ((temp.get(1).charAt(i) >= '0' && temp.get(1).charAt(i) <= '9') || temp.get(1).charAt(i) == '.' || temp.get(1).charAt(i) == '-')
                            tt += temp.get(1).charAt(i);
                    } else {
                        numbers.add(BigDecimal.valueOf(Double.parseDouble(tt)));
                        tt = "";
                    }
                }
                numbers.add(BigDecimal.valueOf(Double.parseDouble(tt)));
                tt = "";
            } else {
                throw new FileIsEmptyException();
            }

            if (operations.size() > 10 || operations.size() < 1) {
                throw new InvalidLengthOperationException();
            } else {
                for (int i = 0; i < operations.size(); i++) {
                    if (operations.get(i) < 1 || operations.get(i) > 6) {
                        throw new NonExistingOperation();
                    }
                }
            }

            if (numbers.size() < 1 || numbers.size() > 20) {
                throw new InvalidLengthNumberException();
            }

            boolean found = false;
            for (Integer operation : operations) {
                if (operation == 3) {
                    if (scanner.hasNextDouble()) {
                        divisor = scanner.nextDouble();
                        if (divisor == 0) {
                            throw new DivisionByZeroException();
                        }
                    } else {
                        throw new InvalDataException();
                    }

                }
            }




//            System.out.println(operations);
//            System.out.println(numbers);
//            System.out.println(divisor);

            double result = 0;
            double resultM = 1;
            int count = 0;

            for(Integer c: operations){
                switch(c) {
                    case 1:
                        for (Number n : numbers) {
                            result += n.doubleValue();
                        }
                        break;
                    case 2:
                        for (Number n : numbers) {
                            resultM *= n.doubleValue();
                        }
                        break;
                    case 3:
                        for (int j = 0; j < numbers.size(); j++) {
                            numbers.set(j,numbers.get(j).doubleValue() / divisor);
                        }
                        break;
                    case 4:
                        for (Number n : numbers) {
                            result += n.doubleValue();
                        }
                        result = result / numbers.size();
                        break;
                    case 5:
                            for (Number number : numbers) {
                                if (number.doubleValue() < 0) {
                                    found = true;
                                    break;
                                }
                            }
                            try {
                                for (int j = 0; j < numbers.size(); j++) {
                                    if (!found) {
                                        numbers.set(j, Math.sqrt(numbers.get(j).doubleValue()));
                                    } else {
                                        throw new NegativeSqrtException();
                                    }
                                }
                            }
                            catch (NegativeSqrtException e){
                                except  = "Exception: Square root cannot be calculated for negative value\n";
                            }
                        break;
                    case 6:
                        numbers = (ArrayList<Number>) numbers.stream().filter(a -> (a.doubleValue() > 0))
                                .collect(Collectors.toList());
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + c);
                }

                if(result != 0){
                    writer.write(result+"\n");
                }
                else if(resultM != 1){
                    writer.write(resultM+"\n");
                }
                else{
                    for (int j = 0; j < numbers.size() - 1; j++) {
                            writer.write(numbers.get(j) + ", ");
                    }
                    check(writer, operations, numbers, count);
                }

                except = null;
                result = 0;
                resultM = 1;
                count++;
            }

        } catch (FileIsEmptyException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (DivisionByZeroException e) {
            except = "Exception: Division by 0";
        } catch (InvalidLengthOperationException e) {
            except = "Exception: The list of operations has an invalid length";
        } catch (NonExistingOperation e) {
            except = "Exception: Non-existing operation";
        } catch (InvalidLengthNumberException e) {
            except = "Exception: The list of numbers has an invalid length";
        }
        catch (InvalDataException e){
            except = "Exception: Invalid data";
        }


        try {
            if (except != null) {
                writer.write(except);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void check(FileWriter writer, ArrayList<Integer> operations, ArrayList<Number> numbers, int count) throws IOException {
        if(count != operations.size()-1) {
            if (numbers.get(numbers.size() - 1).doubleValue() >= 0) {
                writer.write(numbers.get(numbers.size() - 1) + "\n");
            }
        }
        else{
            if (numbers.get(numbers.size() - 1).doubleValue() >= 0) {
                writer.write(numbers.get(numbers.size() - 1)+"");
            }
        }
    }
}
