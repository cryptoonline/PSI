import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by naveed on 2/15/15.
 */
public class ClientPhase2 {

    public static void main(String[] args) {
        String filename = args[0];
	String fileFromServer = args[1];

        ArrayList<BigInteger> sk = (ArrayList<BigInteger>)read("ClientSK.out");
        Paillier paillier = new Paillier();
        paillier.setSecretKey(sk);

        BigInteger[] encryptedPolyEval = (BigInteger[])read(fileFromServer);
        Inputs inputs = new Inputs(filename);
        BigInteger[] roots = inputs.getInputs();

        for(int i = 0; i < encryptedPolyEval.length; i++){
            BigInteger match = paillier.Decryption(encryptedPolyEval[i]);
            if(Arrays.asList(roots).contains(match)){
                System.out.println(match + " is common.");
            }
        }
    }

    public static Object read(String filename){
        ObjectInputStream ois;
        Object object = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(filename));
            object = ois.readObject();
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return  object;
    }

}
