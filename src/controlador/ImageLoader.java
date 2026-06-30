package util;

import javax.swing.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ImageLoader {
    private static final Map<String, ImageIcon> cache = new ConcurrentHashMap<>();
    
    public static ImageIcon getIcon(String path) {
        return cache.computeIfAbsent(path, p -> {
            java.net.URL url = ImageLoader.class.getResource(p);
            return url != null ? new ImageIcon(url) : null;
        });
    }
    
    public static void clearCache() {
        cache.clear();
    }
}