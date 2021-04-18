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

import java.math.BigInteger;

public class EventListener implements Listener {

    static ThreadPool pool = new ThreadPool(20, "Handler pool", false);
    public static Cache<Chunk, BigInteger> changes = new Cache<>();
    
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
        //noinspection SynchronizationOnLocalVariableOrMethodParameter fuck you i know what im doing
        synchronized (chunk) {
            BigInteger i = changes.get(chunk);
            if (i == null) {
                changes.add(chunk, BigInteger.valueOf(1), 10000, new Cache.CacheRetriever<Chunk, BigInteger>() {
                    @Override
                    public BigInteger doRetrieve(BigInteger old, Chunk key) {
                        return null;
                    }
                });
            }
            else {
                changes.add(chunk, i.add(BigInteger.ONE), 10000, new Cache.CacheRetriever<Chunk, BigInteger>() {
                    @Override
                    public BigInteger doRetrieve(BigInteger old, Chunk key) {
                        return null;
                    }
                });
            }
        }
    }
}
