import java.io.FileNotFoundException;

/**
 * Created by Paweł Kowalik on 18.10.2017.
 */
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Neuron neuron=new Neuron("dane.txt","test.txt",2,0.001);
        neuron.test();
    }
}
