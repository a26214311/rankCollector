package senka;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import lib.TimerTask;

public class Search {
	private static String[] shipid2name = new String[]{
			"null","睦月","如月","null","null","null","長月","三日月","null","吹雪","白雪","深雪","磯波","綾波","敷波","曙","潮","陽炎","不知火","黒潮","雪風","長良","五十鈴","由良","大井","北上","扶桑","山城","皐月","文月","菊月","望月","初雪","叢雲","暁","響","雷","電","初春","子日","若葉","初霜","白露","時雨","村雨","夕立","五月雨","涼風","霰","霞","島風","天龍","龍田","名取","川内","神通","那珂","大井改","北上改","古鷹","加古","青葉","妙高","那智","足柄","羽黒","高雄","愛宕","摩耶","鳥海","最上","利根","筑摩","最上改","祥鳳","飛鷹","龍驤","伊勢","金剛","榛名","長門","陸奥","伊勢改","赤城","加賀","霧島","比叡","日向","日向改","鳳翔","蒼龍","飛龍","隼鷹","朧","漣","朝潮","大潮","満潮","荒潮","球磨","多摩","木曾","千歳","千代田","千歳改","千代田改","千歳甲","千代田甲","千歳航","千代田航","翔鶴","瑞鶴","瑞鶴改","鬼怒","阿武隈","夕張","瑞鳳","瑞鳳改","大井改二","北上改二","三隈","三隈改","舞風","衣笠","鈴谷","熊野","伊168","伊58","伊8","鈴谷改","熊野改","大和","秋雲","夕雲","巻雲","長波","大和改","阿賀野","能代","矢矧","酒匂","五十鈴改二","衣笠改二","武蔵","夕立改二","時雨改二","木曾改二","Верный","武蔵改","金剛改二","比叡改二","榛名改二","霧島改二","大鳳","香取","伊401","大鳳改","龍驤改二","川内改二","神通改二","那珂改二","あきつ丸","神威","まるゆ","弥生","卯月","あきつ丸改","磯風","浦風","谷風","浜風","Bismarck","Bismarck改","Bismarck zwei","Z1","Z3","Prinz Eugen","Prinz Eugen改","Bismarck drei","Z1 zwei","Z3 zwei","天津風","明石","大淀","大鯨","龍鳳","時津風","明石改","利根改二","筑摩改二","初風","伊19","那智改二","足柄改二","羽黒改二","綾波改二","飛龍改二","蒼龍改二","霰改二","大潮改二","阿武隈改二","吹雪改","白雪改","初雪改","深雪改","叢雲改","磯波改","綾波改","敷波改","金剛改","比叡改","榛名改","霧島改","天龍改","龍田改","球磨改","多摩改","木曾改","長良改","五十鈴改","由良改","名取改","川内改","神通改","那珂改","陽炎改","不知火改","黒潮改","雪風改","島風改","朧改","曙改","漣改","潮改","暁改","響改","雷改","電改","初春改","子日改","若葉改","初霜改","白露改","時雨改","村雨改","夕立改","五月雨改","涼風改","朝潮改","大潮改","満潮改","荒潮改","霰改","霞改","睦月改","如月改","皐月改","文月改","長月改","菊月改","三日月改","望月改","古鷹改","加古改","青葉改","妙高改","那智改","足柄改","羽黒改","高雄改","愛宕改","摩耶改","鳥海改","利根改","筑摩改","長門改","陸奥改","赤城改","加賀改","蒼龍改","飛龍改","龍驤改","祥鳳改","飛鷹改","隼鷹改","鳳翔改","扶桑改","山城改","翔鶴改","鬼怒改","阿武隈改","千歳航改","千代田航改","夕張改","舞風改","衣笠改","千歳航改二","千代田航改二","null","null","初風改","秋雲改","夕雲改","巻雲改","長波改","阿賀野改","能代改","矢矧改","弥生改","卯月改","Z1改","Z3改","浜風改","谷風改","酒匂改","null","天津風改","浦風改","龍鳳改","妙高改二","磯風改","大淀改","時津風改","春雨改","早霜改","清霜改","初春改二","朝雲改","山雲改","野分改","秋月改","天城","葛城","null","U-511改","null","null","null","null","null","null","null","null","香取改","朝霜改","高波改","照月改","Libeccio改","瑞穂改","風雲改","海風改","江風改","速吸改","Graf Zeppelin改","嵐改","萩風改","鹿島改","初月改","Zara改","沖波改","Iowa改","Pola改","親潮改","春風改","Warspite改","Aquila改","水無月改","伊26改","浦波改","山風改","朝風改","松風改","Commandant Teste改","藤波改","伊13改","伊14改","占守改","国後改","八丈改","石垣改","大鷹改","神鷹改","null","択捉改","松輪改","佐渡改","対馬改","旗風改","null","null","天霧改","狭霧改","Richelieu改","Ark Royal改","Jervis改","Ташкент改","Gambier Bay改","Intrepid改","伊168改","伊58改","伊8改","伊19改","まるゆ改","伊401改","雲龍","春雨","雲龍改","潮改二","隼鷹改二","早霜","清霜","扶桑改二","山城改二","朝雲","山雲","野分","古鷹改二","加古改二","皐月改二","初霜改二","叢雲改二","秋月","照月","初月","高波","朝霜","吹雪改二","鳥海改二","摩耶改二","天城改","葛城改","U-511","Graf Zeppelin","Saratoga","睦月改二","如月改二","呂500","暁改二","Saratoga改","Warspite","Iowa","Littorio","Roma","Libeccio","Aquila","秋津洲","Italia","Roma改","Zara","Pola","秋津洲改","瑞穂","沖波","風雲","嵐","萩風","親潮","山風","海風","江風","速吸","翔鶴改二","瑞鶴改二","朝潮改二","霞改二","鹿島","翔鶴改二甲","瑞鶴改二甲","朝潮改二丁","江風改二","霞改二乙","神風","朝風","春風","松風","旗風","神風改","天龍改二","龍田改二","天霧","狭霧","水無月","null","伊26","浜波","藤波","浦波","鬼怒改二","由良改二","満潮改二","荒潮改二","Commandant Teste","Richelieu","伊400","伊13","伊14","Zara due","白露改二","村雨改二","神威改","神威改母","null","null","鈴谷改二","熊野改二","null","null","null","鈴谷航改二","熊野航改二","null","Гангут","Октябрьская революция","Гангут два","null","Ark Royal","Ташкент","占守","国後","Jervis","null","春日丸","null","null","択捉","松輪","大鷹","岸波","早波","大鷹改二","伊504","佐渡","涼月","null","神鷹","Luigi Torelli","神鷹改二","涼月改","null","UIT-25","対馬","長門改二","夕雲改二","長波改二","Gambier Bay","Saratoga Mk.II","武蔵改二","多摩改二","文月改二","Intrepid","Saratoga Mk.II Mod.2","日振","大東","伊勢改二","日向改二","瑞鳳改二","浦風丁改","磯風乙改","浜風乙改","谷風丁改","瑞鳳改二乙","Samuel B.Roberts","Johnston","巻雲改二","風雲改二","福江","陽炎改二","不知火改二","黒潮改二","null","null","Nelson","null","陸奥改二","Gotland","Maestrale","Nelson改","null","null","Gotland改","Maestrale改","日進","null","峯雲","八丈","石垣","日進甲","null","null","null","G.Garibaldi","金剛改二丙","null","null","赤城改二","null","Fletcher","null","null","赤城改二戊","null","Colorado","null","null","null","Luigi Torelli改","伊400改","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","日振改","大東改","浜波改","Samuel B.Roberts改","null","null","null","福江改","岸波改","峯雲改","早波改","Johnston改","日進改","G.Garibaldi改","Fletcher改","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null"
			};
	private static String[] shipid2name2 = new String[]{
			"null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","null","Colorado改","null","null","null"
			};
	public static void main(String[] args) {
		try {
			System.out.println(searchByName("ぺんちゃん", "29bf3ccb7a96dd585f4b0fcd09823ded279dc8bc", 8));
//			Date t = new Date(new Date().getTime()-3600000*19/2+60000*5);
//			System.out.println(123);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String seekByName(Map<String, String[]> data)throws Exception{
		String name = URLDecoder.decode(data.get("name")[0],"utf-8");
		int server = Integer.valueOf(data.get("server")[0]);
		String token = TimerTask.getToken(server);
		String ret = searchByName(name, token, server);
		return name+"\n当前战果值："+ret+"\n"+"";
	}
	
	public static String searchByName(String name,String token,int server)throws Exception{
		DBCollection cl_n_senka = Util.db.getCollection("cl_n_senka_"+server);
		DBObject senkaData = cl_n_senka.findOne(new BasicDBObject("_id",name));
		Date now = new Date();
		BasicDBList senkalist = (BasicDBList)senkaData.get("d"+now.getMonth());
		if(senkaData!=null){
			Object ido = senkaData.get("id");
			if(ido!=null){
				String ids = ido.toString();
				String[] ida = ids.split(",");
				System.out.println(ido);
				if(ida.length==1){
					return getUserInfoById(ida[0], token, server, senkalist);
				}else if(ida.length<5){
					int c=1;
					Object co = senkaData.get("c");
					if(co!=null){
						c=Integer.valueOf(co.toString());
					}
					String ret = "";
					System.out.println(co);
					if(c<=2){
						for(int i=0;i<ida.length;i++){
							ret = ret + getUserInfoById(ida[i], token, server, senkalist)+"\n\n";
						}
					}else{
						if(c==ida.length){
							ret = ret + getUserInfoByIdMulti(ids, token, server, senkalist,c)+"\n\n";
						}else{
							ret = "此ID情况复杂，无法查询";
						}
					}
					return ret;
				}else{
					
				}
			}
		}
		return "something error";
	}
	
	public static String getUserInfoById(String id,String token,int server,BasicDBList senkalist)throws Exception{
		Date now = new Date();
		DBCollection cl_senka = Util.db.getCollection("cl_senka_"+server);
		DBObject userdata = cl_senka.findOne(new BasicDBObject("_id",Integer.valueOf(id)));
		BasicDBList explist = (BasicDBList)userdata.get("exp");
		int pointer1 = 0;
		int pointer2 = 0;
		JSONObject front = null;
		long nowno = Util.getRankDateNo(now);
		while(pointer1<explist.size()&&pointer2<senkalist.size()){
			DBObject expdata = (DBObject)explist.get(pointer1);
			DBObject senkadata = (DBObject)senkalist.get(pointer2);
			int exp = Integer.valueOf(expdata.get("d").toString());
			int senka = Integer.valueOf(senkadata.get("senka").toString());
			Date expts = (Date)expdata.get("ts");
			int senkano = Integer.valueOf(senkadata.get("ts").toString());
			if(isExpKeyTs(expts)){
				int expno = Util.getRankDateNo(new Date(expts.getTime()+3600000*2));
				if(expts.getYear()*12+expts.getMonth()<now.getYear()*12+now.getMonth()){
					pointer1++;
				}else if(expno==senkano){
					front = new JSONObject();
					front.put("ts", expts);
					front.put("senka", senka);
					front.put("exp", exp);
					break;
				}else if(expno>senkano){
					pointer2++;
				}else{
					pointer1++;
				}
			}else{
				pointer1++;
			}
		}
		pointer1 = 0;
		pointer2 = 0;
		JSONObject tail = null;
		while(pointer1<explist.size()&&pointer2<senkalist.size()){
			DBObject expdata = (DBObject)explist.get(explist.size()-1-pointer1);
			DBObject senkadata = (DBObject)senkalist.get(senkalist.size()-1-pointer2);
			int exp = Integer.valueOf(expdata.get("d").toString());
			int senka = Integer.valueOf(senkadata.get("senka").toString());
			Date expts = (Date)expdata.get("ts");
			int senkano = Integer.valueOf(senkadata.get("ts").toString());
			if(isExpKeyTs(expts)){
				int expno = Util.getRankDateNo(new Date(expts.getTime()+3600000*2));
				System.out.println(expno);
				if(expno==senkano){
					tail = new JSONObject();
					tail.put("ts", expts);
					tail.put("senka", senka);
					tail.put("no", senkadata.get("no"));
					tail.put("exp", exp);
					break;
				}else if(expno>senkano){
					pointer1++;
				}else{
					pointer2++;
				}
			}else{
				pointer1++;
			}
		}
		
		JSONObject jd = getNowExp(Integer.valueOf(id), token, server);
		JSONArray deck = jd.getJSONArray("deck");
		int nowexp = jd.getInt("exp");
		Date tailts = (Date)tail.get("ts");
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		System.out.println(tail);
		String addsenka = "("+tail.getInt("no")+"位)   "+tail.getInt("senka")+"+"+Math.round((nowexp-tail.getInt("exp"))/1000.0*7.0)/10.0
				+"="+(tail.getInt("senka")+Math.round((nowexp-tail.getInt("exp"))/1000.0*7.0)/10.0)+"   ("+sdf.format(tailts)+"----"+sdf.format(now)+")\n";
		Date frontts = (Date)front.get("ts");
		
		if(tailts.getTime()-frontts.getTime()>40000000){
			int senkasub = tail.getInt("senka")-front.getInt("senka");
			int expsub = tail.getInt("exp")-front.getInt("exp");
			addsenka = addsenka + "EX:"+(int)(senkasub-expsub/10000.0*7.0)+"    ("+sdf.format(frontts)+"-----"+sdf.format(tailts)+")";
		}
		String deckinfo = "";
		for(int i=0;i<deck.length();i++){
			JSONObject shipd = deck.getJSONObject(i);
			int aid = shipd.getInt("api_id");
			if(aid>0){
				int shipid = deck.getJSONObject(i).getInt("api_ship_id");
				int lv = deck.getJSONObject(i).getInt("api_level");
				if(shipid<shipid2name.length){
					deckinfo = deckinfo + "lv."+lv+" "+shipid2name[shipid]+";";
				}else if(shipid+1000<shipid2name2.length){
					deckinfo = deckinfo + "lv."+lv+" "+shipid2name2[shipid-1000]+";";
				}else{
					deckinfo = deckinfo + "lv."+lv+" "+"unknown"+";";
				}
				
			}
		}
		pointer1 = 0;
		pointer2 = 0;
		JSONArray ja = new JSONArray();
		Map<Integer, JSONObject> m2j = new HashMap<>();
		while(pointer1<explist.size()&&pointer2<senkalist.size()){
			DBObject expdata = (DBObject)explist.get(pointer1);
			DBObject senkadata = (DBObject)senkalist.get(pointer2);
			int exp = Integer.valueOf(expdata.get("d").toString());
			int senka = Integer.valueOf(senkadata.get("senka").toString());
			Date expts = (Date)expdata.get("ts");
			int senkano = Integer.valueOf(senkadata.get("ts").toString());
			if(isExpKeyTs(expts)){
				int expno = Util.getRankDateNo(new Date(expts.getTime()+3600000*2));
				if(expts.getYear()*12+expts.getMonth()<now.getYear()*12+now.getMonth()){
					pointer1++;
				}else if(expno==senkano){
					JSONObject seekdata = new JSONObject();
					seekdata.put("ts", expts);
					seekdata.put("senka", senka);
					seekdata.put("exp", exp);
					ja.put(seekdata);
					m2j.put(expno, seekdata);
					pointer1++;
					pointer2++;
				}else if(expno>senkano){
					pointer2++;
				}else{
					pointer1++;
				}
			}else{
				pointer1++;
			}
		}
		System.out.println(123123111);
		System.out.println(ja);
		String r = "<table border=4>";
		Date firstDate = new Date();
		firstDate.setDate(1);
		int firstdayofWeek = firstDate.getDay();
		int frontblanknum=(6+firstdayofWeek)%7;
		int days = monthOfDay[firstDate.getMonth()];
		int lines = (int)Math.ceil((double)(days+frontblanknum)/7);
		int len=ja.length();
		for(int i=0;i<lines;i++){
			r=r+"<tr>";
			for(int j=1;j<=7;j++){
				int day = i*7+j-frontblanknum;
				if(day<1){
					r=r+"<td><div></div><div></div></td>";
				}else if(day>days){
					r=r+"<td><div></div><div></div></td>";
			        }else if(day>ja.length()*2){
					r=r+"<td><div></div><div></div></td>";
			        }else{
			        	JSONObject j0=null;
			        	JSONObject j1=null;
			        	JSONObject j2=null;
			        	
			        		j0 = m2j.get(day*2-2);
			        		j1 = m2j.get(day*2-1);
			        		j2 = m2j.get(day*2);
			        	
				        	System.out.println(j0);
				        	System.out.println(j1);
				        	System.out.println(j2);
				        	int add1=(j0==null||j1==null)?0:(j1.getInt("exp")-j0.getInt("exp"))*7/10000;
				        	int add2=(j2==null||j1==null)?0:(j2.getInt("exp")-j1.getInt("exp"))*7/10000;
				        	r=r+"<td><table border=0>";
				        	r=r+"<tr colspan=\"2\"><td><div style=\"text-align:center;font-size:40px\"><b>"+day+"</b></div></td></tr>";
				        	r=r+"<tr><td>"+(j0==null?"--":j0.getInt("senka"))+"</td><td>"+(j1==null?"--":j1.getInt("senka"))+"</td></tr>";
				        	r=r+"<tr><td>+"+add1+"</td><td>+"+add2+"</td></tr>";
				        	r=r+"</table></td>";
			        }
			}
			r=r+"</tr>";
			
		}
		r=r+"</table>";
		return addsenka+"\n"+deckinfo+"\n\n"+r;
	}
	private static int[] monthOfDay = new int[]{31,28,31,30,31,30,31,31,30,31,30,31};
	
	public static String getUserInfoByIdMulti(String ids,String token,int server,BasicDBList senkalist,int c)throws Exception{
		Date now = new Date();
		DBCollection cl_senka = Util.db.getCollection("cl_senka_"+server);
		String[] ida = ids.split(",");
		BasicDBList dbl = new BasicDBList();
		for(int i=0;i<ida.length;i++){
			dbl.add(Integer.valueOf(ida[i]));
		}
		BasicDBObject query = new BasicDBObject("_id",new BasicDBObject("$in",dbl));
		DBCursor dbc = null;
		try {
			dbc = cl_senka.find(query);
			while (dbc.hasNext()) {
				DBObject dbObject = (DBObject) dbc.next();
				BasicDBList explist = (BasicDBList)dbObject.get("exp");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(dbc!=null){
				dbc.close();
			}
		}
		
		return "";
	}
	
	
	
	
	
	
	public static JSONObject getNowExp(int id,String token,int server)throws Exception{
		DBCollection cl_tmp_exp = Util.db.getCollection("cl_tmp_exp");
		Date now = new Date();
		BasicDBObject query = new BasicDBObject("_id",server+"_"+now.getTime()/60000+"_"+id);
		DBObject expData = cl_tmp_exp.findOne(query);
		int exp=0;
		JSONObject jd;
		if(expData==null){
			JSONObject data = getNowExpForce(id, token, server);
			JSONArray expa = data.getJSONArray("api_experience");
			exp = expa.getInt(0);
			query.append("exp", exp);
			query.append("ts", now);
			query.append("info", data.toString());
			cl_tmp_exp.save(query);
			jd=data;
		}else{
			exp = Integer.valueOf(expData.get("exp").toString());
			jd = new JSONObject(expData.get("info").toString());
		}
		JSONArray deck = jd.getJSONObject("api_deck").getJSONArray("api_ships");
		JSONObject jr = new JSONObject();
		jr.put("exp",exp);
		jr.put("deck",deck);
		return jr;
	}
	
	
	
	
	
	public static JSONObject getNowExpForce(int id,String token,int server)throws Exception{
		String path = "/kcsapi/api_req_member/get_practice_enemyinfo";
		String param = "api%5Ftoken="+token+"&api%5Fmember%5Fid="+id+"&api%5Fverno=1";
		try {
			String r = Lib.ApiPost(path, param, token, server);
			if(r.startsWith("svdata="));
			JSONObject jd = new JSONObject(r.substring(7));
			System.out.println(jd);
			JSONObject data = jd.getJSONObject("api_data");
			JSONArray expa = data.getJSONArray("api_experience");
			int exp = expa.getInt(0);
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			return new JSONObject();
		}
	}
	
	
	
	public static boolean isExpKeyTs(Date dat){
		Date  n1 = new Date(dat.getTime()+(dat.getTimezoneOffset()+480)*60000);
		int left = (int)(43200000-(n1.getTime()-18000000)%43200000)/1000;
		if(left<1200||left>43200-1200){
			return true;
		}else{
			return false;
		}
	}

}
