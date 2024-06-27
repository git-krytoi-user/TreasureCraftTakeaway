package dev.r1nex.treasurecrafttakeaway.particles;

import com.comphenix.protocol.wrappers.WrappedParticle;
import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.file.YamlConfiguration;
import dev.r1nex.treasurecrafttakeaway.wrappers.WrapperPlayServerWorldParticles;

import java.awt.*;

public class Particles {

    public void spawnParticle(Location location, YamlConfiguration yamlConfiguration) {
        if (yamlConfiguration.getBoolean("settings.particles.enabled")) {
            Particle particleName = Particle.valueOf(yamlConfiguration.getString("settings.particles.particle"));
            WrapperPlayServerWorldParticles particle = new WrapperPlayServerWorldParticles();
            particle.setParticleType(WrappedParticle.create(particleName, 0));
            particle.setX((float) location.getX());
            particle.setY((float) location.getY() + 0.54);
            particle.setZ((float) location.getZ());
            particle.setLongDistance(true);
            particle.broadcastPacket();
        }
    }
}
