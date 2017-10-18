import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Paweł Kowalik on 18.10.2017.
 */

public class Neuron {

    private List<Double>[] dendrites;
    private List<Double> synapses;
    private List<Double> teacher = new ArrayList<>();
    private String nameOfTeacherFile,nameOfTestFile;
    private double learningFactor;

    Neuron(String nameOfTeacherFile, String nameOfTestFile, int howManyDendrites, double learningFactor)
    {
        this.dendrites=new List[howManyDendrites];
        this.synapses=new ArrayList<>(howManyDendrites);
        for(int i=0;i<howManyDendrites;++i){
            dendrites[i]=new ArrayList<>();
        }
        this.learningFactor=learningFactor;
        this.nameOfTeacherFile=nameOfTeacherFile;
        this.nameOfTestFile=nameOfTestFile;
    }

    private void readFromFile(List[] dendrites, String nameOfFile, boolean test) {
        String []parts;
        Scanner read = null;
        try {
            read = new Scanner(new File(nameOfFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String text=read.nextLine();

        if(test==false) {
            do {
                parts = text.split(";");
                for (int j = 0; j < dendrites.length + 1; ++j) {
                    if (j < dendrites.length)
                        dendrites[j].add(Double.parseDouble(parts[j]));
                    else teacher.add(Double.parseDouble(parts[j]));
                }
                text = read.nextLine();
            } while (read.hasNextLine());
        }else{
            do {
                parts = text.split(";");
                for (int j = 0; j < dendrites.length; ++j) {
                    dendrites[j].add(Double.parseDouble(parts[j]));
                }
                text = read.nextLine();
            } while (read.hasNextLine());
        }
    }

    private void setSynapses(){
        for(int i=0;i<dendrites.length;++i){
            synapses.add(Math.random());
        }
    }

    private double getMembranePotential(int index){
        double sum=0;
        for(int i=0;i<synapses.size();++i){
            sum+=dendrites[i].get(index)*synapses.get(i);
        }
        return sum;
    }

    private int getOutput(int index)
    {
        if(getMembranePotential(index) >= 0)
            return 1;
        else return -1;
    }

    private double learn() throws FileNotFoundException {
        readFromFile(dendrites, nameOfTeacherFile,false);
        setSynapses();

        double limit = 0;
        double error;

        do {
            error = 0;
            for (int i = 0; i < teacher.size(); ++i) {
                if (getOutput(i) != teacher.get(i)) {
                    for (int j = 0; j < synapses.size(); ++j) {
                        synapses.set(j, (synapses.get(j) + (learningFactor * dendrites[j].get(i) * (teacher.get(i) - getOutput(i)))));
                    }
                    error =1;
                }
            }
            ++limit;
        } while (limit < 9999999 && error != 0);

        if (limit < 9999999) {
            return limit;
        } else {
            return -1.0;
        }
    }

    public void test() throws FileNotFoundException {
        double results=learn();
        for(int i=dendrites.length-1;i>=0;--i)
        {
            for(int j=teacher.size()-1;j>=0;--j){
                dendrites[i].remove(j);
            }
        }
        readFromFile(dendrites,nameOfTestFile,true);
        int error=0;

        for(int i=0;i<teacher.size();++i){
            System.out.println("Liczby: ");
            for(int j=0;j<dendrites.length;++j){
                System.out.println(dendrites[j].get(i)+" ");
            }
            System.out.println("odpowiedz: "+getOutput(i)+" poprawna odpowiedz: "+teacher.get(i));
            if(teacher.get(i)!=getOutput(i))error++;
        }
        System.out.println("");
        if(results<99999&&results>0)
        {
            System.out.println("Perceptron nauczył się po "+(int)results+" probach.");
        }else{
            System.out.println("Perceptron nie nauczył się.");
        }
        System.out.println("Test wykonano dla operacji LICZBA<LICZBA");
        System.out.println("Ilosc dendrytow: "+dendrites.length);
        System.out.println("Ilosc synaps: "+synapses.size());
        System.out.println("Wspolczynnik uczenia sie: "+learningFactor);
        System.out.println("Ilosc bledow: "+error);
    }
}
