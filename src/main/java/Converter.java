import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

public class Converter {

    public static void main(String[] args) {
        //rasporyazheniye -- У3.data.yml
        //perechen_narushenii -- D32.data.yml
        //akt -- U54.data.yml

        String rasporyazheniye = args[0];
        String perechen_narushenii = args[1];
        String input_akt = args[2];
        String output_akt = args[3];

        try {
            byte[] dataArr = FileUtils.readFileToByteArray(new File(rasporyazheniye));
            String yaml_rasporyazheniya = new String(dataArr, StandardCharsets.UTF_8);

            dataArr = FileUtils.readFileToByteArray(new File(perechen_narushenii));
            String yaml_narushenii = new String(dataArr, StandardCharsets.UTF_8);

            dataArr = FileUtils.readFileToByteArray(new File(input_akt));
            String yaml_akta = new String(dataArr, StandardCharsets.UTF_8);

            String json_rasporyazheniya = convertYamlToJson(yaml_rasporyazheniya);

            JSONObject jsonObj = new JSONObject(json_rasporyazheniya);

            JSONObject meta = (JSONObject) jsonObj.get("meta");

            String id_osnovaniya = meta.get("uuid").toString();

            JSONObject data = (JSONObject) jsonObj.get("data");

            String osnovanie = ((JSONObject) data.get("display_name")).get("value").toString();

            String id_zastroishchika = ((JSONObject) data.get("naim_zastrojshhika")).get("id").toString();

            String zastroishchik = ((JSONObject) data.get("naim_zastrojshhika")).get("display_name").toString();

            String id_podryadchika = ((JSONObject) data.get("naim_podryadchika")).get("id").toString();

            String podryadchik = ((JSONObject) data.get("naim_podryadchika")).get("display_name").toString();

            String id_obyekta_ks = ((JSONObject) data.get("obqqekt_ks_list_naim")).get("id").toString();

            String obyekt_ks = ((JSONObject) data.get("obqqekt_ks_list_naim")).get("display_name").toString();

            String data_nachala_proverki = ((JSONObject) data.get("data_nachala_proverki")).get("value").toString();

            String data_zaversheniya_proverki = ((JSONObject) data.get("data_zaversheniya_proverki")).get("value").toString();

            List<String> upoln_sotrudniki = new ArrayList<>();
            for(int i = 0; i < ((JSONArray) data.get("l3")).length(); i++) {
                String sotrudnik = ((JSONObject) ((JSONObject) ((JSONArray) data.get("l3")).get(i))
                        .get("upolnomochennyhq_sotrudnik")).get("display_name").toString();
                 upoln_sotrudniki.add(sotrudnik);
            }

            List<String> prigl_eksperty = new ArrayList<>();
            for(int i = 0; i < ((JSONArray) data.get("l3_1")).length(); i++) {
                String ekspert = ((JSONObject) ((JSONObject) ((JSONArray) data.get("l3_1")).get(i))
                        .get("fio_ehksperta_sp")).get("display_name").toString();
                prigl_eksperty.add(ekspert);
            }

            String json_narushenii = convertYamlToJson(yaml_narushenii);

            jsonObj = new JSONObject(json_narushenii);

            data = (JSONObject) jsonObj.get("data");

            String id_zayavitelya = ((JSONObject) data.get("zayavitelq")).get("id").toString();

            String zayavitel = ((JSONObject) data.get("zayavitelq")).get("display_name").toString();

            //List<String> vyyavl_narusheniya = new ArrayList<>();
//            for(int i = 0; i < ((JSONArray) data.get("ls_narusheniya")).length(); i++) {
//                String narusheniye = ((JSONObject) ((JSONObject) ((JSONArray) data.get("ls_narusheniya")).get(i))
//                        .get("kod")).get("value").toString();
//                vyyavl_narusheniya.add(narusheniye);
//            }

            JSONArray vyyavl_narusheniya = (JSONArray) data.get("ls_narusheniya");

            //vyyavl_narusheniya.stream().forEach(System.out::println);

            String json_akta = convertYamlToJson(yaml_akta);

            jsonObj = new JSONObject(json_akta);

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

            ((JSONObject) data.get("obshhaya_prodolzhitelqnostq_proverki"))
                    .put("value", String.valueOf(duration.toDays()));

            ((JSONObject) data.get("obshhaya_prodolzhitelqnostq_proverki_tip")).put("id", 1);
            ((JSONObject) data.get("obshhaya_prodolzhitelqnostq_proverki_tip")).put("display_name", "рабочих дней");

            if(vyyavl_narusheniya.length() > 0) {
                ((JSONObject) data.get("vyhevlennye_narusheniya_tip")).put("id", 1);
                ((JSONObject) data.get("vyhevlennye_narusheniya_tip")).put("display_name", "выявлено");
                JSONArray spisok_narushenii = data.getJSONArray("ls_s20");

                Iterator<Object> iter = vyyavl_narusheniya.iterator();
                while (iter.hasNext()) {

                    JSONObject iteriruyemoye_narusheniye = (JSONObject) iter.next();
                    JSONObject obyekt_narusheniya = new JSONObject();

                    int index = 0;
                    for(int j = 0; j < spisok_narushenii.length(); j++) {
                        JSONObject o = (JSONObject) spisok_narushenii.get(j);
                        String kod_iz_spiska = ((JSONObject) o.get("kod")).get("value").toString();
                        String kod_iz_iteratsii = ((JSONObject) iteriruyemoye_narusheniye.get("kod"))
                                .get("value").toString();
                        if(kod_iz_iteratsii.equals(kod_iz_spiska)) {
                            index = j;
                            break;
                        }
                    }

                    JSONObject tipovoe_narushenie = new JSONObject();
                    tipovoe_narushenie.put("id", ((JSONObject) iteriruyemoye_narusheniye.get("kod"))
                            .get("value"));
                    tipovoe_narushenie.put("display_name", ((JSONObject) iteriruyemoye_narusheniye.get("kod"))
                            .get("value"));
                    obyekt_narusheniya.put("tipovoe_narushenie", tipovoe_narushenie);

                    JSONObject naimenovaniye_narusheniya = new JSONObject();
                    naimenovaniye_narusheniya.put("value", ((JSONObject) ((JSONObject) spisok_narushenii.get(index))
                            .get("narushenie")).get("value"));
                    obyekt_narusheniya.put("naim_narush", naimenovaniye_narusheniya);

                    JSONObject normativnyi_akt_narusheniya = new JSONObject();
                    normativnyi_akt_narusheniya.put("value", ((JSONObject) ((JSONObject) spisok_narushenii.get(index))
                            .get("npa")).get("value"));
                    obyekt_narusheniya.put("norm_akt_narush", normativnyi_akt_narusheniya);

                    ((JSONArray) data.get("vyhyavlennyhe_narusheniya")).put(obyekt_narusheniya);
                }
            }
            //System.out.println(jsonObj.toString(4));

            yaml_akta = convertJsonToYaml(jsonObj.toString(4));

            FileWriterWithEncoding fw = new FileWriterWithEncoding(
                    new File(output_akt), StandardCharsets.UTF_8);
            fw.write(yaml_akta);
            fw.flush();
            fw.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static String convertYamlToJson(String yaml) throws JsonProcessingException, IOException {
        ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
        Object obj = yamlReader.readValue(yaml, Object.class);
        ObjectMapper jsonWriter = new ObjectMapper();
        return jsonWriter.writeValueAsString(obj);
    }

    private static String convertJsonToYaml(String jsonString) throws JsonProcessingException, IOException {
        JsonNode jsonNodeTree = new ObjectMapper().readTree(jsonString);
        String jsonAsYaml = new YAMLMapper().disable(YAMLGenerator.Feature.SPLIT_LINES).writeValueAsString(jsonNodeTree);
        return jsonAsYaml;
    }
}
