package net.cogzmc.core.player;

import net.cogzmc.core.util.Point;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;

import java.net.InetAddress;
import java.util.Map;

/**
 * A class to represent a player on the server.
 *
 * @since 1.0
 * @author Joey
 * @see net.cogzmc.core.player.COfflinePlayer
 * @see net.cogzmc.core.player.CPermissible
 */
public interface CPlayer extends COfflinePlayer {
    /**
     * Gets the name of the player on the server.
     * @return A {@link java.lang.String} representing the player's name.
     */
    String getName();

    /**
     * Gets their current IP Address
     * @return The {@link java.net.InetAddress} representing their IP Address.
     */
    InetAddress getAddress();

    /**
     * Gets the status of the player (online or offline)
     * @return A boolean representing the player's online state.
     */
    boolean isOnline();

    /**
     * Gets if the player is joining our server for the first time.
     * @return A boolean denoting if the player is joining the server for the first time.
     */
    boolean isFirstJoin();

    /**
     * Sends any number of messages to the player.
     * @param messages The messages you wish to be sent to the player.
     */
    void sendMessage(String... messages);

    /**
     * Sends a message that is balanced vertically in the center as best as possible (upward aligned for odd numbered line counts) and fills the entire 10 line chat window.
     * @param messageLines The lines of text you want to send to the user.
     */
    void sendFullChatMessage(String... messageLines);

    /**
     * Clears the entire chat (50 lines) for the player by sending blank messages.
     */
    void clearChatAll();

    /**
     * Clears the visible chat (20 lines) for the player by sending blank messages.
     */
    void clearChatVisible();

    /**
     * Plays a sound for the player.
     * @param s The sound to play.
     * @param volume How loud to play it (distance heard).
     * @param pitch The pitch to play it at (speed).
     */
    void playSoundForPlayer(Sound s, Float volume, Float pitch);

    /**
     * Plays a sound for the player
     * @param s The sound to play.
     * @param volume How loud to play it (distance heard).
     */
    void playSoundForPlayer(Sound s, Float volume);

    /**
     * Plays a sound for the player
     * @param s The sound to play.
     */
    void playSoundForPlayer(Sound s);

    /**
     * Gives a new item to specification to this player.
     * @param material The {@link org.bukkit.Material} that the item will be.
     * @param quantity The amount of the item to give to the player.
     * @param title The title of the item as shown in the inventory (display name).
     * @param lore The lore of the item, will be wrapped automatically.
     * @param enchantments The enchantments to put onto this item, this will be done unsafely and as such can be <b>any</b> enchantments.
     * @param slot The slot to put the item into. Pass {@code null} for any slot.
     */
    void giveItem(Material material, Integer quantity, String title, String lore, Map<Enchantment, Integer> enchantments, Integer slot);

    /**
     * Gives a new item to specification to this player.
     *
     * Defaults {@code slot} to {@code null} from the overloaded {@link #giveItem(org.bukkit.Material, Integer, String, String, java.util.Map, Integer)}
     * @param material The {@link org.bukkit.Material} that the item will be.
     * @param quantity The amount of the item to give to the player.
     * @param title The title of the item as shown in the inventory (display name).
     * @param lore The lore of the item, will be wrapped automatically.
     * @param enchantments The enchantments to put onto this item, this will be done unsafely and as such can be <b>any</b> enchantments.
     */
    void giveItem(Material material, Integer quantity, String title, String lore, Map<Enchantment, Integer> enchantments);

    /**
     * Gives a new item to specification to this player.
     *
     * Defaults {@code enchantments} to {@code null} from the overloaded {@link #giveItem(org.bukkit.Material, Integer, String, String, java.util.Map)}
     * @param material The {@link org.bukkit.Material} that the item will be.
     * @param quantity The amount of the item to give to the player.
     * @param title The title of the item as shown in the inventory (display name).
     * @param lore The lore of the item, will be wrapped automatically.
     */
    void giveItem(Material material, Integer quantity, String title, String lore);

    /**
     * Gives a new item to specification to this player.
     *
     * Defaults {@code lore} to {@code null} from the overloaded {@link #giveItem(org.bukkit.Material, Integer, String, String)}
     * @param material The {@link org.bukkit.Material} that the item will be.
     * @param quantity The amount of the item to give to the player.
     * @param title The title of the item as shown in the inventory (display name).
     */
    void giveItem(Material material, Integer quantity, String title);

    /**
     * Gives a new item to specification to this player.
     *
     * Defaults {@code quantity} to {@code 1} from the overloaded {@link #giveItem(org.bukkit.Material, Integer, String)}
     * @param material The {@link org.bukkit.Material} that the item will be.
     * @param title The title of the item as shown in the inventory (display name).
     */
    void giveItem(Material material, String title);

    /**
     * Gives a new item to specification to this player.
     *
     * Defaults {@code title} to {@code null} from the overloaded {@link #giveItem(org.bukkit.Material, Integer, String)}
     * @param material The {@link org.bukkit.Material} that the item will be.
     * @param quantity The amount of the item to give to the player.
     */
    void giveItem(Material material, Integer quantity);

    /**
     * Gives a new item to specification to this player.
     *
     * Defaults {@code quantity} to {@code 1} from the overloaded {@link #giveItem(org.bukkit.Material, Integer)}
     * @param material The {@link org.bukkit.Material} that the item will be.
     */
    void giveItem(Material material);

    /**
     * Adds a status effect to this player with the given parameters.
     * @param type The type of {@link org.bukkit.potion.PotionEffectType} to give to the player.
     * @param level The level of potion effect to give to the player (1 = I, 2 = II, etc)
     * @param ticks The number of ticks to give the potion effect for.
     * @param ambient If the potion effect is "ambient" or not.
     *
     * @see org.bukkit.potion.PotionEffect
     */
    void addStatusEffect(PotionEffectType type, Integer level, Integer ticks, Boolean ambient);

    /**
     * Adds a status effect to this player with the given parameters.
     *
     * Defaults {@code ambient} to {@code false} from the overloaded {@link #addStatusEffect(org.bukkit.potion.PotionEffectType, Integer, Integer, Boolean)}
     * @param type The type of {@link org.bukkit.potion.PotionEffectType} to give to the player.
     * @param level The level of potion effect to give to the player (1 = I, 2 = II, etc)
     * @param ticks The number of ticks to give the potion effect for.
     */
    void addStatusEffect(PotionEffectType type, Integer level, Integer ticks);

    /**
     * Adds a status effect to this player with the given parameters.
     *
     * Defaults {@code ticks} to {@code MAX_VALUE} or infinite status effect length from the overloaded {@link #addStatusEffect(org.bukkit.potion.PotionEffectType, Integer, Integer)}
     * @param type The type of {@link org.bukkit.potion.PotionEffectType} to give to the player.
     * @param level The level of potion effect to give to the player (1 = I, 2 = II, etc)
     */
    void addStatusEffect(PotionEffectType type, Integer level);

    /**
     * Adds a status effect to this player with the given parameters.
     *
     * Defaults {@code level} to {@code 1} from the overloaded {@link #addStatusEffect(org.bukkit.potion.PotionEffectType, Integer)}
     * @param type The type of {@link org.bukkit.potion.PotionEffectType} to give to the player.
     */
    void addStatusEffect(PotionEffectType type);

    /**
     * Removes a status effect from the player.
     * @param type The {@link org.bukkit.potion.PotionEffectType} to remove from the player.
     */
    void removeStatusEffect(PotionEffectType type);

    /**
     * Resets the state of the player.
     */
    void resetPlayer();

    /**
     * Gives you the representation of the player from Bukkit using the {@link org.bukkit.entity.Player} object.
     * @return The player from Bukkit.
     */
    Player getBukkitPlayer();

    /**
     * Gets you a {@link net.cogzmc.core.player.CooldownManager} for watching the time an action is performed by the player.
     * @return The cooldown manager.
     */
    CooldownManager getCooldownManager();

    /**
     * Returns where the player is currently standing, based off Bukkit's getLocation value.
     * @return A point representing where the player is currently standing.
     */
    Point getPoint();

    /**
     * Gets the point of the block where the player is currently standing. Returns whole numbers for all dimensions, and lacks a pitch and yaw (set to 0)
     * @return A point object representing the player's current block location.
     */
    Point getBlockPoint();

    /**
     * Creates a <b>new</b> {@link net.cogzmc.core.player.COfflinePlayer} to represent this player in the event that
     * you wish to store this player without keeping a reference to the {@link org.bukkit.entity.Player}.
     * @return The {@link net.cogzmc.core.player.COfflinePlayer} representation of this player.
     */
    COfflinePlayer getNewOfflinePlayer();

    /**
     * Gets an object which can be used to determine where a player is located (based off the maximind database)
     * @return The {@link net.cogzmc.core.player.GeoIPManager.GeoIPInfo} object.
     */
    GeoIPManager.GeoIPInfo getGeoIPInfo();

    /**
     * Do not call this method, it will invoke code intended for player's who have just joined the server.
     */
    void onJoin();

    /**
     * Sets the name above a players head to a specific value. This will not be stored in the database. {@code null} will reset this value.
     * @param tagName The tag name
     * @deprecated Unable to use in MC 1.8
     */
    @Deprecated void setTagName(String tagName);

    /**
     * By default this is the players username, unless set manually by {@link #setTagName(String)}.
     * @return The player's tag name
     */
    @Deprecated String getTagName();

    /**
     * Removes a player's tag name.
     */
    @Deprecated void removeTagName();

    /**
     * returns if a player has a different tab name than their actual name.
     * @return If the player has a tag name.
     */
    @Deprecated boolean hasTagName();

    /**
     * Sets a tag prefix for above their head.
     * @param tagPrefix Tag prefix to set
     */
    void setTagPrefix(String tagPrefix);


    /**
     * Returns the currently assigned tag prefix
     * @return  Current tag prefix
     */
    String getTagPrefix();

    /**
     * Sets a new scoreboard to this CPlayer safely, while preserving everyones tag prefix
     * @param sb    Scoreboard to set
     */
    @Deprecated
    void setScoreboard(Scoreboard sb);

    /**
     * Current scoreboard that is assigned to this player
     * @return  Current scoreboard
     */
    @Deprecated
    Scoreboard getScoreboard();


    /**
     * Retrieves the scoreboard manager linked with this player
     * @return  Scoreboard manager for this player
     */
    public CPlayerScoreboardManager getScoreboardManager();

    /**
     * Forces the player to disconnect from the server. You are encouraged to use this method over the {@link org.bukkit.entity.Player#kickPlayer(String)} method.
     * @param message The message to appear for the the player whom is being disconnected.
     */
    void kickPlayer(String message);
}
