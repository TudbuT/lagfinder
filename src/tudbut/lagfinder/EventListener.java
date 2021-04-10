package tudbut.lagfinder;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;
import tudbut.obj.TLMap;
import tudbut.tools.Cache;
import tudbut.tools.Lock;
import tudbut.tools.ThreadPool;

public class EventListener implements Listener {

    static ThreadPool pool = new ThreadPool(20, "Handler pool", false);
    public static Cache<Chunk, Long> changes = new Cache<>();
    
    public void onAction(Event event) {
        if(event instanceof BlockEvent) {
            onAction(((BlockEvent) event).getBlock());
        }
        if(event instanceof EntityEvent) {
            onAction(((EntityEvent) event).getEntity());
        }
        if(event instanceof PlayerEvent) {
            onAction(((PlayerEvent) event).getPlayer());
        }
    }
    
    private void onAction(Block block) {
        Chunk chunk = block.getChunk();
        onAction(chunk);
    }
    
    private void onAction(Entity entity) {
        Chunk chunk = entity.getLocation().getChunk();
        onAction(chunk);
    }
    
    private void onAction(Chunk chunk) {
        synchronized (chunk) {
            Long i = changes.get(chunk);
            if (i == null) {
                changes.add(chunk, 1L, 10000, new Cache.CacheRetriever<Chunk, Long>() {
                    @Override
                    public Long doRetrieve(Long old, Chunk key) {
                        return null;
                    }
                });
            }
            else {
                changes.add(chunk, i + 1L, 10000, new Cache.CacheRetriever<Chunk, Long>() {
                    @Override
                    public Long doRetrieve(Long old, Chunk key) {
                        return null;
                    }
                });
            }
        }
    }
}
