/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.mavenproject2;

/**
 *
 * @author Дзюба
 */
import static java.lang.Double.NaN;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.StatUtils;

public class DataManipulator {
     private static DescriptiveStatistics CreateStat (ArrayList<Double> s){
            DescriptiveStatistics Statistic = new DescriptiveStatistics();
        for (double a : s) {
            Statistic.addValue(a);
        }
        return Statistic;
    }
    
    public static ArrayList<ArrayList<Object>> OneList(ArrayList<ArrayList<Double>> data) {//данные одного листа
        ArrayList<ArrayList<Object>> results = new ArrayList<>();//создает пустой список для записи результатов
        int max_len = -1;
        for (int i = 0; i < data.size(); ++i){//находит максимальную длину списка из всех списков, содержащихся в samples
            if (data.get(i).size()>max_len){
                max_len = data.get(i).size();
            }
        }
        for (int i = 0; i < data.size(); ++i){//заполняет все списки в samples дополнительными элементами до максимальной длины (если длина списка меньше максимальной) и выводит размеры списков в консоль.
            while(data.get(i).size() < max_len){
                data.get(i).add(0.0);
            }
           // System.out.println(samples.get(i).size());
        }
        for (ArrayList<Double> sample : data) {//перебор списков из samples
            //с помощью цикла for-each. Для каждого списка вызывается метод CountingSample,
            //который рассчитывает статистические параметры и добавляет их в список param, соответствующий данному списку из samples.
            results.add(CountingSample(sample));
        }
       Corelation(data, results);//вычисляет корреляцию между всеми парами
       //списков из samples и добавляет результаты в список param.
        return results;
   
    }
    
    public static void Corelation(ArrayList<ArrayList<Double>> data,ArrayList<ArrayList<Object>> params ) {
        PearsonsCorrelation corrPearson = new PearsonsCorrelation();//объект класса PearsonsCorrelation для вычисления коэффициента корреляции Пирсона.
        for (int i = 0; i < data.size(); ++i){
            ArrayList<Object> dataParam = params.get(i);
            ArrayList<Double> s = data.get(i);
              try {//вычислить коэффициент корреляции и ковариацию между s и следующим списком в samples (на основе индекса next_i).
                int next_i = (i+1)%data.size();
                double[] X = s.stream().mapToDouble(d -> d).toArray();//X и Y, в которые копирует данные из списков s и samples.get(next_i) с помощью метода mapToDouble.
                double[] Y = data.get(next_i).stream().mapToDouble(d -> d).toArray();
                dataParam.add(corrPearson.correlation(X,Y));//добавляет полученное значение в список sampleParam.
                Covariance c = new Covariance();
                dataParam.add(c.covariance(X,Y));
            } catch (MathIllegalArgumentException ex) {//метод добавляет значения null в список sampleParam для коэффициента корреляции и ковариации.
                dataParam.add(null);
                dataParam.add(null);
            }
         }
      }
    
    
    public static  ArrayList<Object> CountingSample(ArrayList<Double> data){
            ArrayList<Object> dataParam = new ArrayList<>();
  
       
            DescriptiveStatistics newStat = CreateStat(data);
         
            
            double min = newStat.getMin();
            double max = newStat.getMax();
            
            
            //1. Рассчитать среднее геометрическое для каждой выборки 
           double[] vals = new double[data.size()];
           for (int i = 0; i < data.size(); i++) {
            vals[i] = data.get(i);
            }
            double geomMean = StatUtils.geometricMean(vals);
            if (Double.isNaN(geomMean)) {
           geomMean = 0;
           }
           dataParam.add((geomMean));
   
            
            //2.	Рассчитать среднее арифметическое для каждой выборки 
            double mean = newStat.getMean();
            dataParam.add(mean);
            //3.	Рассчитать оценку стандартного отклонения для каждой выборки
            double standardDeviation = newStat.getStandardDeviation();
            dataParam.add((standardDeviation));
            //4.	Рассчитать размах каждой выборки                        
            dataParam.add(((max - min)));
            //6.	Рассчитать количество элементов в каждой выборке     
            dataParam.add(newStat.getN());
            //7.	Рассчитать дисперсию для каждой выборки
            dataParam.add((newStat.getVariance()));
            //8.	Рассчитать для каждой выборки построить доверительный интервал для мат. ожидания (Случайные числа подчиняются нормальному закону распределения)
//            sampleParam.add((ms.getPercentile(5)));
//            sampleParam.add((ms.getPercentile(95)));

            TDistribution t = new TDistribution(data.size()-1);
            double tlevel = t.inverseCumulativeProbability(0.05);
            double conf = tlevel*standardDeviation/Math.sqrt(data.size());
            dataParam.add(mean - conf);
            dataParam.add(mean + conf);
            //9.	Рассчитать коэффицент вариации для каждой выборки 
            dataParam.add((standardDeviation/ Math.abs(mean)));
            //10.	Рассчитать максимумы и минимумы для каждой выборки
            dataParam.add((min));
            dataParam.add((max));
            //5.	Рассчитать коэффициенты ковариации для всех пар случайных чисел
            PearsonsCorrelation ps = new PearsonsCorrelation(); 
            
       // System.out.println(sampleParam);
        
        return dataParam;
        
    }
    

    public static DefaultTableModel MakeDTM(ArrayList<ArrayList<Object>> params,
        ArrayList<String> name, String[] nameParam, int next){//вывод результатов в дерево
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Параметры");
        for (int i = 0; i < name.size(); ++i){
            model.addColumn(name.get(i));
        }
        
        for (int i = 0; i < nameParam.length; i++) {
            Object[] values = new Object[name.size()+1];
            values[0] = nameParam[i];
            for (int j = 0; j < name.size(); j++){
                values[j+1] = params.get(j).get(i + next);
               // System.out.print(values[j+1] + " ");
            }
            //System.out.println("***");
            model.addRow(values);
        }
        return model;
    }
}
