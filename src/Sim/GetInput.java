package Sim;
import java.io.*;

public class GetInput {

    int numOfVec = 3;
    private String[] nameList = new String[numOfVec];
    private String[] sideList = new String[numOfVec];
    private String[] vehicleList = new String[numOfVec];
    private String[] positionList = new String[numOfVec];
    private String[] velocityList = new String[numOfVec];
    private String[] isRadarList = new String[numOfVec];
    private String[] radarRangeList = new String[numOfVec];
    private String[][] listsList;

    public GetInput() {
        listsList = new String[][] {
                nameList,
                sideList,
                vehicleList,
                positionList,
                velocityList,
                isRadarList,
                radarRangeList
        };
    }
    public void readInput(String filePath) {
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            for(int i = 0; i<3; i++){
                for(int j = 0; j<7; j++){
                    String line = br.readLine();
                    listsList[j][i] = line;
                    }
                }

            /*
            for(int i = 0; i<3; i++) {
                for (int j = 0; j < 7; j++) {
                    System.out.println(listsList[j][i]);
                }
            }
            */
            } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        /*
        for(String line; (line = br.readLine()) != null; ) {
                System.out.println(line);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        */
    }


}

