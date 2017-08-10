package tw.zhuran.balsam.parser;


import io.vertx.core.json.Json;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import tw.zhuran.balsam.skeleton.BiqugeSkeleton;
import tw.zhuran.balsam.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
    public static void catalog(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        BiqugeSkeleton skeleton = new BiqugeSkeleton();
        String title = doc.select(skeleton.titleSelector).first().text();
        String author = doc.select(skeleton.authorSelector).first().text();
        Elements items = doc.select(skeleton.volumeSelector).first().children();
        List<Object> currentChapters = new ArrayList<>();
        List<Object> volumes = new ArrayList<>();
        Map currentVolume = null;
        String bookId = Util.randomHash();
        for (Element item : items) {
            if (item.tagName().equals(skeleton.volumeTag)) {
                if (!currentChapters.isEmpty()) {
                    volumes.add(currentVolume);
                } else {
                    String currentVolumeTitle = item.text();
                    String volumeId = Util.randomHash();
                    currentVolume = map("chapters", currentChapters, "title", currentVolumeTitle, "bookId", bookId, "volumeId", volumeId);
                }
            } else if (item.tagName().equals(skeleton.chapterTag)) {
                String chapterId = Util.randomHash();
                String chapterTitle = item.select("a").first().text();
                Map<String, Object> chapter = map("chapterId", chapterId, "volumeId", currentVolume.get("volumeId"), "bookId", bookId, "title", chapterTitle);
                currentChapters.add(chapter);
            }
        }
        Map<String, Object> book = map("bookId", bookId, "author", author, "title", title, "volumes", volumes);
        System.out.println(Json.encode(book));
    }

    public static Map<String, Object> map(Object... args) {
        Map<String, Object> map = new HashMap<>();
        for (int i  = 0; i < args.length; i += 2) {
            String key = args[i].toString();
            Object value;
            if (i + 1 == args.length) {
                value = null;
            } else {
                value = args[i + 1];
            }
            map.put(key, value);
        }
        return map;
    }
}
