package net.cogzmc.core.util;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.cogzmc.core.json.PointSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

@Data
@RequiredArgsConstructor(staticName = "of")
public final class Point implements Cloneable {
    @Getter private final static PointSerializer serializer = new PointSerializer();

    @NonNull private Double x;
    @NonNull private Double y;
    @NonNull private Double z;
    @NonNull private Float pitch;
    @NonNull private Float yaw;

    public boolean isBlock() {
        return (pitch == 0.0f && yaw == 0.0f && y % 1 == 0 && z % 1 == 0 && x % 1 == 0);
    }

    public Location getLocation(World world) {
        return new Location(world, x, y, z, yaw, pitch);
    }

    public static Point of(Double x, Double y, Double z) {
        return Point.of(x, y, z, 0f, 0f);
    }

    public static Point of(Location location) {
        return Point.of(location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw());
    }

    public static Point of(Block block) {
        return Point.of(block.getLocation());
    }

    public Double distanceSquared(Point point) {
        Double x = Math.pow((this.x-point.getX()), 2);
        Double y = Math.pow((this.y-point.getY()), 2);
        Double z = Math.pow((this.z-point.getZ()), 2);
        return x + y + z;
    }

    public Double distance(Point point) {
        return Math.sqrt(distanceSquared(point));
    }

    public Point deepCopy() {
        return new Point(x, y, z, pitch, yaw);
    }

    public Point add(Double x, Double y, Double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Point subtract(Double x, Double y, Double z) {
        return add(-1*x, -1*y, -1*z);
    }

    public Point add(Point point) {
        return add(point.getX(), point.getY(), point.getZ());
    }

    public Point subtract(Point point) {
        return subtract(point.getX(), point.getY(), point.getZ());
    }
}
