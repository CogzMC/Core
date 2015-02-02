package net.cogzmc.gameapi.model.game;

import com.google.common.collect.ImmutableSet;
import lombok.*;
import net.cogzmc.core.Core;
import net.cogzmc.core.effect.enderBar.EnderBarManager;
import net.cogzmc.core.modular.ModularPlugin;
import net.cogzmc.core.player.COfflinePlayer;
import net.cogzmc.core.player.CPlayer;
import net.cogzmc.gameapi.GameAPI;
import net.cogzmc.gameapi.model.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * This class serves to represent a game that can be played. The rules of a game are modified through the delegated calls in the two
 */
@SuppressWarnings("UnusedDeclaration")
@Data
@Setter(AccessLevel.NONE)
public class Game<ArenaType extends Arena> {
    private final ArenaType arena; //Arena this is being played in
    @Setter @NonNull private GameActionDelegate<ArenaType> actionDelegate; //Action delegate; we tell this once something happens
    @Setter @NonNull private GameRuleDelegate<ArenaType> ruleDelegate; //Rule delegate; we tell this before something happens
    private final ModularPlugin owner;
    private final GameMeta meta;
    private final String prefix;
    private final GameAPI gameAPI;
    private final GameListener listener;
    private final DamageTracker<ArenaType> damageTracker;
    /**
     * For the initial players of the game (meaning, anyone who started the game playing) or anyone that joined the game to hold a "player" status.
     *
     * Spectators that join the game without ever first being a "player" are not included in this set.
     */
    @Getter(AccessLevel.NONE) private final Set<COfflinePlayer> participants;
    /**
     * Current players of the game.
     */
    @Getter(AccessLevel.NONE) private final Set<CPlayer> players;
    /**
     * Current spectators of the game.
     */
    @Getter(AccessLevel.NONE) private final Set<CPlayer> spectators;
    private ImmutableSet<GameObserver> observers = ImmutableSet.of();
    /**
     * Holds the current running countdowns.
     */
    @Getter(AccessLevel.NONE) private final Set<GameCountdown> runningCountdowns = new HashSet<>();

    private boolean loaded = false;
    private boolean running;
    private Date timeStarted;

    /**
     * Creates a new Game with all of the passed options.
     * @param owner The plugin who is managing this game.
     * @param arena The arena in which you wish to play the game.
     * @param players The players who will be initial participants in the game.
     * @param actionDelegate The action delegate for this game.
     * @param ruleDelegate The rule delegate for this game.
     */
    public Game(ModularPlugin owner, ArenaType arena, Set<CPlayer> players, GameActionDelegate<ArenaType> actionDelegate,
                GameRuleDelegate<ArenaType> ruleDelegate, GameMeta meta) {
        this.owner = owner;
        this.arena = arena;
        this.actionDelegate = actionDelegate;
        this.ruleDelegate = ruleDelegate;
        this.meta = meta;
        this.players = new HashSet<>(players);
        spectators = new HashSet<>();
        participants = new HashSet<>();
        gameAPI = Core.getInstance().getModuleProvider(GameAPI.class);
        assert gameAPI != null;
        prefix = getAPIFormat("prefix", false);
        for (CPlayer player : this.players) {
            this.participants.add(player.getNewOfflinePlayer());
        }
        listener = new GameListener(this);
        damageTracker = new DamageTracker<>(this);
        observers = getNewObservers();
    }

    private ImmutableSet<GameObserver> getNewObservers(GameObserver... add) {
        ImmutableSet.Builder<GameObserver> builder = ImmutableSet.builder();
        builder.add(actionDelegate);
        builder.addAll(observers);
        builder.add(add);
        builder.add(damageTracker);
        return builder.build();
    }

    public final void registerObserver(GameObserver observer) {
        observers = getNewObservers(observer);
    }

    /**
     * Gets the players who are actively participating in the game.
     * @return A {@link com.google.common.collect.ImmutableSet} of {@link net.cogzmc.core.player.CPlayer} instances.
     */
    public final ImmutableSet<CPlayer> getPlayers() {
        return ImmutableSet.copyOf(players);
    }

    /**
     * Gets the active spectators of the game.
     * @return A {@link com.google.common.collect.ImmutableSet} of {@link net.cogzmc.core.player.CPlayer} instances.
     */
    public final ImmutableSet<CPlayer> getSpectators() {
        return ImmutableSet.copyOf(spectators);
    }

    /**
     * Gets players who were once members of the {@code players} {@link java.util.Set}
     * @return A {@link com.google.common.collect.ImmutableSet} of {@link net.cogzmc.core.player.COfflinePlayer} instances.
     */
    public final ImmutableSet<COfflinePlayer> getParticipants() {
        return ImmutableSet.copyOf(participants);
    }

    public final boolean isPlaying(CPlayer player) {
        return players.contains(player);
    }

    public final boolean isSpectating(CPlayer player) {
        return spectators.contains(player);
    }

    public final boolean isInvolvedInGame(CPlayer player) {
        return spectators.contains(player) || players.contains(player);
    }

    /**
     * Gets the Bukkit players involved in this game.
     * @return An {@link com.google.common.collect.ImmutableSet} of Bukkit's {@link org.bukkit.entity.Player} objects.
     */
    public final ImmutableSet<Player> getBukkitPlayers() {
        HashSet<Player> players1 = new HashSet<>();
        for (CPlayer player : players) {
            players1.add(player.getBukkitPlayer());
        }
        return ImmutableSet.copyOf(players1);
    }

    /**
     * Loads the game
     */
    public void load() {
        if (loaded) throw new IllegalStateException("The game has already been loaded!");
        arena.load();
    }

    /**
     * Call this to start the game.
     */
    public void start() {
        this.timeStarted = new Date();
        this.running = true;
        Bukkit.getPluginManager().registerEvents(listener, owner);
        ImmutableSet<CPlayer> players1 = getPlayers();
        for (GameObserver gameObserver : getObservers()) {
            try {gameObserver.onGameStart();} catch (Exception e) {e.printStackTrace();}
            for (CPlayer player : players1) {
                try {gameObserver.onPlayerJoinGame(player);} catch (Exception e) {e.printStackTrace();}
            }
        }
    }

    final void gameCountdownStarted(GameCountdown countdown) {
        runningCountdowns.add(countdown);
    }

    final void gameCountdownEnded(GameCountdown countdown) {
        runningCountdowns.remove(countdown);
    }

    void playerLeft(CPlayer player) {
        players.remove(player);
        spectators.remove(player);
        for (GameObserver gameObserver : getObservers()) {
            try {gameObserver.onPlayerLeaveGame(player);} catch (Exception e) {e.printStackTrace();}
        }
        ensureGameCapacity();
    }

    private void ensureGameCapacity() {
        if (getPlayers().size() > 1) return;
        finishGame();
    }

    private String formatUsingMeta(String original) {
        return original;
    }

    private String getModularFormat(ModularPlugin plugin, String key, Boolean prefix, String[]... formatters) {
        String format = plugin.getFormat(key, formatters);
        for (String[] formatter : formatters) {
            if (formatter.length != 2) continue;
            format = format.replaceAll(formatter[0], formatter[1]);
        }
        if (prefix) format = this.prefix + formatUsingMeta(format);
        return ChatColor.translateAlternateColorCodes('&', format);
    }

    /**
     * This is used to get a format that is shared by the GameAPI games.
     * @param key The formatter key
     * @param prefix The prefix
     * @param formatters The formatters
     * @return A formatted string from the key.
     */
    public final String getAPIFormat(String key, Boolean prefix, String[]... formatters) {
        return getModularFormat(gameAPI, key, prefix, formatters);
    }

    public final String getAPIFormat(String key, String[]... formatters) {
        return getAPIFormat(key, true, formatters);
    }

    public final String getPluginFormat(String key, Boolean prefix, String[]... formatters) {
        return getModularFormat(owner, key, prefix, formatters);
    }

    public final String getPluginFormat(String key, String[]... formatters) {
        return getPluginFormat(key, true, formatters);
    }

    public final void addSpectator(CPlayer player) {
        if (players.contains(player)) throw new IllegalArgumentException("Call makePlayerSpectator instead!");
        spectators.add(player);
        transformSpectator(player);
        for (GameObserver gameObserver : getObservers()) {
            try {gameObserver.onSpectatorJoinGame(player);} catch (Exception e) {e.printStackTrace();}
        }

    }

    public final void makePlayerSpectator(CPlayer player) {
        if (!players.contains(player)) throw new IllegalArgumentException("Call addSpectator instead.");
        players.remove(player);
        spectators.add(player);
        transformSpectator(player);
        for (GameObserver gameObserver : getObservers()) {
            try {gameObserver.onPlayerBecomeSpectator(player);} catch (Exception e) {e.printStackTrace();}
        }

    }

    private void transformSpectator(CPlayer player) {
        Player bukkitPlayer = player.getBukkitPlayer();
        GameUtils.hidePlayerFromPlayers(bukkitPlayer, getBukkitPlayers());
        player.resetPlayer();
        player.addStatusEffect(PotionEffectType.INVISIBILITY, 2);
        player.playSoundForPlayer(Sound.ORB_PICKUP, 10f, 3f);
        bukkitPlayer.setAllowFlight(true);
        bukkitPlayer.setFlying(true);
        player.giveItem(Material.BOOK, getAPIFormat(""));
    }

    public final void finishGame() {
        stopGame(GameCompleteCause.COMPLETION);
    }

    public void stopGame(GameCompleteCause cause) {
        for (GameObserver gameObserver : getObservers()) {
            try {gameObserver.onGameEnd();} catch (Exception e) {e.printStackTrace();}
        }
    }

    public final void broadcastSound(Sound sound) {
        broadcastSound(sound, 0f);
    }

    public final void broadcastSound(Sound sound, Float pitch) {
        for (CPlayer cPlayer : getPlayers()) {
            cPlayer.playSoundForPlayer(sound, 10f, pitch);
        }
    }

    public final void broadcast(String... messages) {
        for (CPlayer cPlayer : getPlayers()) {
            cPlayer.sendMessage(messages);
        }
    }

    protected final void broadcastEnderBarText(String message) {
        EnderBarManager enderBarManager = Core.getEnderBarManager();
        for (CPlayer cPlayer : getPlayers()) {
            enderBarManager.setTextFor(cPlayer, message);
        }
    }

    public final void broadcastEnderBarHealth(Float percentage) {
        EnderBarManager enderBarManager = Core.getEnderBarManager();
        for (CPlayer cPlayer : getPlayers()) {
            enderBarManager.setHealthPercentageFor(cPlayer, percentage);
        }

    }
}
