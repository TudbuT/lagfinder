package tudbut.lagfinder;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;
import tudbut.obj.TLMap;
import tudbut.parsing.TudSort;
import tudbut.tools.Cache;
import tudbut.tools.ReflectUtil;
import tudbut.tools.StringTools;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class Main extends JavaPlugin {
    
    EventListener listener = new EventListener();
    
    @Override
    public void onEnable() {
        ArrayList<Class<? extends Event>> classesList = new ArrayList<>();
        
        // All block events i hate my life
        classesList.add(BlockBreakEvent.class);
        classesList.add(BlockBurnEvent.class);
        classesList.add(BlockCanBuildEvent.class);
        classesList.add(BlockDamageEvent.class);
        classesList.add(BlockDispenseEvent.class);
        classesList.add(BlockExpEvent.class);
        classesList.add(BlockExplodeEvent.class);
        classesList.add(BlockFadeEvent.class);
        classesList.add(BlockFormEvent.class);
        classesList.add(BlockFromToEvent.class);
        classesList.add(BlockGrowEvent.class);
        classesList.add(BlockIgniteEvent.class);
        classesList.add(BlockMultiPlaceEvent.class);
        classesList.add(BlockPhysicsEvent.class);
        classesList.add(BlockPistonExtendEvent.class);
        classesList.add(BlockPistonRetractEvent.class);
        classesList.add(BlockPlaceEvent.class);
        classesList.add(BlockRedstoneEvent.class);
        classesList.add(BlockSpreadEvent.class);
        classesList.add(CauldronLevelChangeEvent.class);
        classesList.add(EntityBlockFormEvent.class);
        classesList.add(LeavesDecayEvent.class);
        classesList.add(NotePlayEvent.class);
        classesList.add(SignChangeEvent.class);
        
        // All entity events i hate my life
        classesList.add(AreaEffectCloudApplyEvent.class);
        classesList.add(CreatureSpawnEvent.class);
        classesList.add(CreeperPowerEvent.class);
        classesList.add(EnderDragonChangePhaseEvent.class);
        classesList.add(EntityBreakDoorEvent.class);
        classesList.add(EntityBreedEvent.class);
        classesList.add(EntityChangeBlockEvent.class);
        classesList.add(EntityCombustEvent.class);
        classesList.add(EntityCreatePortalEvent.class);
        classesList.add(EntityDamageEvent.class);
        classesList.add(EntityDeathEvent.class);
        classesList.add(EntityExplodeEvent.class);
        classesList.add(EntityInteractEvent.class);
        classesList.add(EntityPickupItemEvent.class);
        classesList.add(EntityPortalEnterEvent.class);
        classesList.add(EntityPortalEvent.class);
        classesList.add(EntityPortalExitEvent.class);
        classesList.add(EntityRegainHealthEvent.class);
        classesList.add(EntityResurrectEvent.class);
        classesList.add(EntityShootBowEvent.class);
        classesList.add(EntitySpawnEvent.class);
        classesList.add(EntityTameEvent.class);
        classesList.add(EntityTargetEvent.class);
        classesList.add(EntityTeleportEvent.class);
        classesList.add(EntityToggleGlideEvent.class);
        classesList.add(EntityUnleashEvent.class);
        classesList.add(ExpBottleEvent.class);
        classesList.add(ExplosionPrimeEvent.class);
        classesList.add(FireworkExplodeEvent.class);
        classesList.add(FoodLevelChangeEvent.class);
        classesList.add(HorseJumpEvent.class);
        classesList.add(ItemDespawnEvent.class);
        classesList.add(ItemMergeEvent.class);
        classesList.add(ItemSpawnEvent.class);
        classesList.add(LingeringPotionSplashEvent.class);
        classesList.add(PigZapEvent.class);
        classesList.add(PlayerDeathEvent.class);
        classesList.add(PlayerLeashEntityEvent.class);
        classesList.add(PotionSplashEvent.class);
        classesList.add(ProjectileHitEvent.class);
        classesList.add(ProjectileLaunchEvent.class);
        classesList.add(SheepDyeWoolEvent.class);
        classesList.add(SheepRegrowWoolEvent.class);
        classesList.add(SlimeSplitEvent.class);
        classesList.add(SpawnerSpawnEvent.class);
        classesList.add(VillagerAcquireTradeEvent.class);
        classesList.add(VillagerReplenishTradeEvent.class);
    
        // All player events cuz bukkit is a bitch and didnt extend the entity events i REALLY hate my life
        classesList.add(PlayerArmorStandManipulateEvent.class);
        classesList.add(PlayerBedEnterEvent.class);
        classesList.add(PlayerBedLeaveEvent.class);
        classesList.add(PlayerBucketEmptyEvent.class);
        classesList.add(PlayerBucketFillEvent.class);
        classesList.add(PlayerDropItemEvent.class);
        classesList.add(PlayerEggThrowEvent.class);
        classesList.add(PlayerFishEvent.class);
        classesList.add(PlayerInteractEvent.class);
        classesList.add(PlayerMoveEvent.class);
        classesList.add(PlayerPickupArrowEvent.class);
        classesList.add(PlayerShearEntityEvent.class);
        classesList.add(PlayerSwapHandItemsEvent.class);
        classesList.add(PlayerToggleSneakEvent.class);
        classesList.add(PlayerToggleSprintEvent.class);
        classesList.add(PlayerUnleashEntityEvent.class);
        classesList.add(PlayerVelocityEvent.class);
    
        System.out.println("Indexing " + classesList.size() + " classes...");
        for (Class<? extends Event> clazz : classesList) {
            Bukkit.getPluginManager().registerEvent(clazz, listener, EventPriority.NORMAL, (ignored0, event) -> listener.onAction(event), this);
        }
        System.out.println("Done");
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            sender.sendMessage("Scanning and sorting...");
            new Thread(() -> {
                Cache<Chunk, BigInteger> map = EventListener.changes.flip().flip();
                Chunk[] chunks = map.keys().toArray(new Chunk[0]);
                chunks = TudSort.sort(chunks, chunk -> map.get(chunk).divide(BigInteger.valueOf(5000)).longValueExact(), false);
                sender.sendMessage("Done.");
                sender.sendMessage(chunks.length + " chunks scanned.");
                try {
                    Thread.sleep(2000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (Chunk chunk : chunks) {
                    sender.sendMessage(chunk.getX() * 16 + " " + chunk.getZ() * 16 + ": " + map.get(chunk));
                }
            }).start();
        } catch (Exception e) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public void onDisable() {
        EventListener.pool.stop();
    }
}
