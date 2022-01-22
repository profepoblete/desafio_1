/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desafio_1_nivel_1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author cesar
 */
public class Desafio_1_nivel_1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws URISyntaxException {
        String archivoOrigen ="";
        String archivoDestino="";
        if(args.length == 2)
        {
            archivoOrigen = args[0];
            archivoDestino = args[1];
        }
        else {
            System.out.println("Debe ingresar nombre de archivo origen y archivo destino");
        }
        JSONParser parser = new JSONParser();

        try {
           
            File origen = new File(Desafio_1_nivel_1.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            String ruta = origen.getParent();
            //leer archivo json
            JSONObject objFechas = (JSONObject) parser.parse(new FileReader(ruta+"\\"+archivoOrigen));
            LinkedHashMap<String, Object> mapFechas = new LinkedHashMap<String, Object>();
            JSONArray fechas = (JSONArray) objFechas.get("fechas");
            //agrega "fechas" a lista de fechas
            ArrayList<String> liFechas = new ArrayList<String>();
            fechas.forEach((f) -> {
                liFechas.add(f.toString());
            });

            // Generar intevalo de fechas en ArrayList
            ArrayList<String> fechasAll = new ArrayList<String>();
            String mes_creacion = objFechas.get("fechaCreacion").toString().substring(0, 7);
            String mes_fin = objFechas.get("fechaFin").toString().substring(0, 7);;

            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate start = LocalDate.parse(mes_creacion + "-01", df);
            LocalDate end = LocalDate.parse(mes_fin + "-01", df);
            for (LocalDate d = start; d.isBefore(end.plusMonths(1)); d = d.plusMonths(1)) {
                fechasAll.add(d.with(TemporalAdjusters.firstDayOfMonth()).format(df));
            }
            //quitar de la lista del intervalo las fechas entregadas
            fechasAll.removeAll(liFechas);

            

            //Agregar a "fechasFaltantes" la lista de fechas que faltan en el intervalo
            JSONArray listaFechasFaltantes = new JSONArray();
            fechasAll.forEach(f -> {
                listaFechasFaltantes.add(f);
            });

            //ordenar elementos en mapa
            mapFechas.put("id", objFechas.get("id"));
            mapFechas.put("fechaCreacion", objFechas.get("fechaCreacion"));
            mapFechas.put("fechaFin", objFechas.get("fechaFin"));
            mapFechas.put("fechas", liFechas);
            mapFechas.put("fechasFaltantes", listaFechasFaltantes);

            try {

                // Escribo mapa en archivo para ordenar los elementos 
                File file = new File(ruta+"\\"+archivoDestino);
                file.createNewFile();
                FileWriter fileWriter = new FileWriter(file);
                
               

                fileWriter.write(mapFechas.toString());
                fileWriter.flush();
                fileWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
