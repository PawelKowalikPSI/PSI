/**
 * Created by Paweł Kowalik on 15.10.2017.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Neuron {

    private double[][] Dendrites;
    private double[] Synapses;
    private int[] Dane;
    private String[] parts;

    public Neuron()
    {
        this.Dendrites=new double[100][2];
        this.Synapses=new double[2];
        this.Dane=new int[100];
    }

    public void read_from_file(){
        try {
            Scanner odczyt = new Scanner(new File("dane.txt"));
            String text=odczyt.nextLine();
            for(int i=0;i<100;++i){
                parts = text.split(";");
                Dendrites[i][0]=(Double.parseDouble(parts[0]));
                Dendrites[i][1]=(Double.parseDouble(parts[1]));
                Dane[i]=(Integer.parseInt(parts[2]));
                if(odczyt.hasNextLine())text = odczyt.nextLine();
            }
            
            

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
    
    public void set_Synapses(){
        for(int i=0;i<2;++i){
            Synapses[i]=Math.random();
        }
    }

    public double getMembranePotential(int index)
    {
        double sum=0;
        for(int i=0;i<2;++i) {
            sum += Dendrites[index][i]*Synapses[i];
        }
        return sum;
    }

    public int getOutput(int index)
    {
        if(getMembranePotential(index) >= 0)
            return 1;
        else return -1;
    }

    public void learn_and_test(){
        read_from_file();
        set_Synapses();

        double tempErr=0;
        double Err;
        int limit=0;

        do {
            Err=0;
            limit++;
            for (int i = 0; i < 100; ++i) {
                tempErr = Dane[i] - getOutput(i);

                for(int j=0;j<2;++j)
                {
                    Synapses[j]+=0.00001*Dendrites[i][j]*(Dane[i]-getOutput(i));
                }

                Err+=tempErr;
            }
        }while (Err!=0&&limit<99999);

        if(limit<99999)
        {
            System.out.println("Perceptron nauczył się po "+limit+" probach.");
        }else{
            System.out.println("Perceptron nie nauczył się.");
        }

//        ------------------------------------------------------------------------------------
        try {
            Scanner odczyt = new Scanner(new File("dane.txt"));
            String text=odczyt.nextLine();
            for(int i=0;i<100;++i){
                parts = text.split(";");
                Dendrites[i][0]=(Double.parseDouble(parts[0]));
                Dendrites[i][1]=(Double.parseDouble(parts[1]));
                Dane[i]=(Integer.parseInt(parts[2]));
                if(odczyt.hasNextLine())text = odczyt.nextLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int blad=0;

        for(int i=0;i<100;++i){
            System.out.println("Liczba "+Dendrites[i][0]+" < "+Dendrites[i][1]+" poprawna odpowiedz: "+Dane[i]+" odpowiedz: "+getOutput(i));
            if(Dane[i]!=getOutput(i))blad++;
        }
        System.out.println("Ilosc bledow: "+blad);

    }


}
