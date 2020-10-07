import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.json.JSONArray;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Converter {

    public static void main(String[] args) {
        try {
            byte[] dataArr = FileUtils.readFileToByteArray(new File("input/У3.data_6.yml"));
            String yaml_rasporyazheniya = new String(dataArr, StandardCharsets.UTF_8);

            dataArr = FileUtils.readFileToByteArray(new File("input/D32.data_1.yml"));
            String yaml_narushenii = new String(dataArr, StandardCharsets.UTF_8);

            dataArr = FileUtils.readFileToByteArray(new File("input/U54.data_1.yml"));
            String yaml_akta = new String(dataArr, StandardCharsets.UTF_8);

//            Pattern nonASCII = Pattern.compile("[^\\x00-\\x7f]+|[\\x00-\\x20]+");
//            yaml = nonASCII.matcher(yaml).replaceAll("");

            String json_rasporyazheniya = convertYamlToJson(yaml_rasporyazheniya);

//            Yaml yamlObj= new Yaml();
//            Object loadedYaml = yamlObj.load(yaml);
//
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            String json = gson.toJson(loadedYaml, LinkedHashMap.class);
//
            //System.out.println(json);

            //JSONObject jsonObject = new JSONObject(JSONObject.valueToString(json));
            //jsonObject.toString(4);

            JSONObject jsonObj = new JSONObject(json_rasporyazheniya);
            //System.out.println(jsonObj.toString(4));

            JSONObject meta = (JSONObject) jsonObj.get("meta");

            String id_osnovaniya = meta.get("uuid").toString();

            System.out.println(id_osnovaniya);

            JSONObject data = (JSONObject) jsonObj.get("data");

            String osnovanie = ((JSONObject) data.get("display_name")).get("value").toString();

            System.out.println(osnovanie);

            String id_zastroishchika = ((JSONObject) data.get("naim_zastrojshhika")).get("id").toString();

            System.out.println(id_zastroishchika);

            String zastroishchik = ((JSONObject) data.get("naim_zastrojshhika")).get("display_name").toString();

            System.out.println(zastroishchik);

            String id_podryadchika = ((JSONObject) data.get("naim_podryadchika")).get("id").toString();

            System.out.println(id_podryadchika);

            String podryadchik = ((JSONObject) data.get("naim_podryadchika")).get("display_name").toString();

            System.out.println(podryadchik);

            String id_obyekta_ks = ((JSONObject) data.get("obqqekt_ks_list_naim")).get("id").toString();

            System.out.println(id_obyekta_ks);

            String obyekt_ks = ((JSONObject) data.get("obqqekt_ks_list_naim")).get("display_name").toString();

            System.out.println(obyekt_ks);

//            List<String> zayaviteli = new ArrayList<>();
//            for(int i = 0; i < ((JSONArray) data.get("ls_d32")).length(); i++) {
//                String zayavitel = ((JSONObject) ((JSONObject) ((JSONArray) data.get("ls_d32")).get(i))
//                        .get("fyao_naim")).get("value").toString();
////                System.out.println(zayavitel);
//                if(!"null".equals(zayavitel)) {
//                    zayaviteli.add(zayavitel);
//                }
//            }
//
//            zayaviteli.stream().forEach(System.out::println);

            String data_nachala_proverki = ((JSONObject) data.get("data_nachala_proverki")).get("value").toString();

            System.out.println(data_nachala_proverki);

            String data_zaversheniya_proverki = ((JSONObject) data.get("data_zaversheniya_proverki")).get("value").toString();

            System.out.println(data_zaversheniya_proverki);

            List<String> upoln_sotrudniki = new ArrayList<>();
            for(int i = 0; i < ((JSONArray) data.get("l3")).length(); i++) {
                String sotrudnik = ((JSONObject) ((JSONObject) ((JSONArray) data.get("l3")).get(i))
                        .get("upolnomochennyhq_sotrudnik")).get("display_name").toString();
                 upoln_sotrudniki.add(sotrudnik);
            }

            upoln_sotrudniki.stream().forEach(System.out::println);

            List<String> prigl_eksperty = new ArrayList<>();
            for(int i = 0; i < ((JSONArray) data.get("l3_1")).length(); i++) {
                String ekspert = ((JSONObject) ((JSONObject) ((JSONArray) data.get("l3_1")).get(i))
                        .get("fio_ehksperta_sp")).get("display_name").toString();
                prigl_eksperty.add(ekspert);
            }

            prigl_eksperty.stream().forEach(System.out::println);

            String json_narushenii = convertYamlToJson(yaml_narushenii);

            jsonObj = new JSONObject(json_narushenii);
            //System.out.println(jsonObj.toString(4));

            data = (JSONObject) jsonObj.get("data");

            String id_zayavitelya = ((JSONObject) data.get("zayavitelq")).get("id").toString();

            System.out.println(id_zayavitelya);

            String zayavitel = ((JSONObject) data.get("zayavitelq")).get("display_name").toString();

            System.out.println(zayavitel);

            List<String> vyyavl_narusheniya = new ArrayList<>();
            for(int i = 0; i < ((JSONArray) data.get("ls_narusheniya")).length(); i++) {
                String narusheniye = ((JSONObject) ((JSONObject) ((JSONArray) data.get("ls_narusheniya")).get(i))
                        .get("kod")).get("value").toString();
                vyyavl_narusheniya.add(narusheniye);
            }

            vyyavl_narusheniya.stream().forEach(System.out::println);

            String json_akta = convertYamlToJson(yaml_akta);

            jsonObj = new JSONObject(json_akta);
            //System.out.println(jsonObj.toString(4));

            data = (JSONObject) jsonObj.get("data");

            ((JSONObject) data.get("osnovanie_proverki")).put("id", id_osnovaniya);
            ((JSONObject) data.get("osnovanie_proverki")).put("display_name", osnovanie);

            ((JSONObject) data.get("naim_zastrojshhika")).put("id", id_zastroishchika);
            ((JSONObject) data.get("naim_zastrojshhika")).put("display_name", zastroishchik);

            ((JSONObject) data.get("naim_podryadchika")).put("id", id_podryadchika);
            ((JSONObject) data.get("naim_podryadchika")).put("display_name", podryadchik);

            ((JSONObject) data.get("obqqekt_ks_list_naim")).put("id", id_obyekta_ks);
            ((JSONObject) data.get("obqqekt_ks_list_naim")).put("display_name", obyekt_ks);

            ((JSONObject) data.get("zayavitelq")).put("id", id_zayavitelya);
            ((JSONObject) data.get("zayavitelq")).put("display_name", zayavitel);

            String beginDateString = data_nachala_proverki;
            String endDateString = data_zaversheniya_proverki;
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date beginDate = format.parse(beginDateString);
            Date endDate = format.parse(endDateString);
            Duration duration = Duration.between(beginDate.toInstant(), endDate.toInstant());
            System.out.println(duration.toDays());

            ((JSONObject) data.get("obshhaya_prodolzhitelqnostq_proverki"))
                    .put("value", String.valueOf(duration.toDays()));

            ((JSONObject) data.get("obshhaya_prodolzhitelqnostq_proverki_tip")).put("id", 1);
            ((JSONObject) data.get("obshhaya_prodolzhitelqnostq_proverki_tip")).put("display_name", "рабочих дней");

            //System.out.println(jsonObj.toString(4));

            yaml_akta = convertJsonToYaml(jsonObj.toString(4));
            System.out.println(yaml_akta);

            //System.out.println(new JSONObject(convertYamlToJson(yaml_akta)).toString(4));

            FileWriterWithEncoding fw = new FileWriterWithEncoding(
                    new File("output/U54.data.yml"), StandardCharsets.UTF_8);
            fw.write(yaml_akta);
            fw.flush();
            fw.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static String convertYamlToJson(String yaml) throws JsonProcessingException, IOException {
        ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());

        //Map<String, Object>
        Object obj = yamlReader.readValue(yaml, Object.class);
                        //.replace("\\", "\\\\"),
                //new TypeReference<Map<String, Object>>(){});
        ObjectMapper jsonWriter = new ObjectMapper();
        return jsonWriter.writeValueAsString(obj);
    }

    private static String convertJsonToYaml(String jsonString) throws JsonProcessingException, IOException {
        JsonNode jsonNodeTree = new ObjectMapper().readTree(jsonString);
        String jsonAsYaml = new YAMLMapper().disable(YAMLGenerator.Feature.SPLIT_LINES).writeValueAsString(jsonNodeTree);
        return jsonAsYaml;
    }
}
