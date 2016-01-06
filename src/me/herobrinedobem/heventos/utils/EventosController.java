package me.herobrinedobem.heventos.utils;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.batataquente.BatataQuente;
import me.herobrinedobem.heventos.eventos.Evento;
import me.herobrinedobem.heventos.frog.Frog;
import me.herobrinedobem.heventos.killer.Killer;
import me.herobrinedobem.heventos.minamortal.MinaMortal;
import me.herobrinedobem.heventos.paintball.Paintball;
import me.herobrinedobem.heventos.spleef.Spleef;

public class EventosController {

	public Evento eventoOcorrendo = null;
	private BatataQuente batataQuenteOcorrendo = null;
	private MinaMortal minaMortalOcorrendo = null;
	private Spleef spleefOcorrendo = null;
	private Frog frogOcorrendo = null;
	private Killer killerOcorrendo = null;
	private Paintball paintballOcorrendo = null;

	private final HEventos instance;

	public EventosController(final HEventos instance) {
		this.instance = instance;
	}

	public Evento loadEvento(final String eventoname) {
		try {
			final File fileEvento = new File(this.instance.getDataFolder().getAbsolutePath() + "/Eventos/" + eventoname + ".yml");
			final YamlConfiguration configEvento = YamlConfiguration.loadConfiguration(fileEvento);
			return new Evento(configEvento);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public BatataQuente loadEventoBatataQuente(final String eventoname) {
		try {
			final File fileEvento = new File(this.instance.getDataFolder().getAbsolutePath() + "/Eventos/" + eventoname + ".yml");
			final YamlConfiguration configEvento = YamlConfiguration.loadConfiguration(fileEvento);
			return new BatataQuente(configEvento);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Paintball loadEventoPaintBall(final String eventoname) {
		try {
			final File fileEvento = new File(this.instance.getDataFolder().getAbsolutePath() + "/Eventos/" + eventoname + ".yml");
			final YamlConfiguration configEvento = YamlConfiguration.loadConfiguration(fileEvento);
			return new Paintball(configEvento);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Frog loadEventoFrog(final String eventoname) {
		try {
			final File fileEvento = new File(this.instance.getDataFolder().getAbsolutePath() + "/Eventos/" + eventoname + ".yml");
			final YamlConfiguration configEvento = YamlConfiguration.loadConfiguration(fileEvento);
			return new Frog(configEvento);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Spleef loadEventoSpleef(final String eventoname) {
		try {
			final File fileEvento = new File(this.instance.getDataFolder().getAbsolutePath() + "/Eventos/" + eventoname + ".yml");
			final YamlConfiguration configEvento = YamlConfiguration.loadConfiguration(fileEvento);
			return new Spleef(configEvento);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public MinaMortal loadEventoMinaMortal(final String eventoname) {
		try {
			final File fileEvento = new File(this.instance.getDataFolder().getAbsolutePath() + "/Eventos/" + eventoname + ".yml");
			final YamlConfiguration configEvento = YamlConfiguration.loadConfiguration(fileEvento);
			return new MinaMortal(configEvento);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Killer loadEventoKiller(final String eventoname) {
		try {
			final File fileEvento = new File(this.instance.getDataFolder().getAbsolutePath() + "/Eventos/" + eventoname + ".yml");
			final YamlConfiguration configEvento = YamlConfiguration.loadConfiguration(fileEvento);
			return new Killer(configEvento);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean hasEvento(final String evento) {
		try {
			return new File(this.instance.getDataFolder().getAbsolutePath() + "/Eventos/" + evento + ".yml").exists();
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public Killer getKillerOcorrendo() {
		return this.killerOcorrendo;
	}

	public void setKillerOcorrendo(final Killer killerOcorrendo) {
		this.killerOcorrendo = killerOcorrendo;
	}

	public Frog getFrogOcorrendo() {
		return this.frogOcorrendo;
	}

	public void setFrogOcorrendo(final Frog frogOcorrendo) {
		this.frogOcorrendo = frogOcorrendo;
	}

	public Paintball getPaintballOcorrendo() {
		return this.paintballOcorrendo;
	}

	public void setPaintBallOcorrendo(final Paintball paintballOcorrendo) {
		this.paintballOcorrendo = paintballOcorrendo;
	}

	public MinaMortal getMinaMortalOcorrendo() {
		return this.minaMortalOcorrendo;
	}

	public void setMinaMortalOcorrendo(final MinaMortal minaMortalOcorrendo) {
		this.minaMortalOcorrendo = minaMortalOcorrendo;
	}

	public BatataQuente getBatataQuenteOcorrendo() {
		return this.batataQuenteOcorrendo;
	}

	public void setBatataQuenteOcorrendo(final BatataQuente batataQuenteOcorrendo) {
		this.batataQuenteOcorrendo = batataQuenteOcorrendo;
	}

	public Evento getEventoOcorrendo() {
		return this.eventoOcorrendo;
	}

	public void setEventoOcorrendo(final Evento eventoOcorrendo) {
		this.eventoOcorrendo = eventoOcorrendo;
	}

	public Spleef getSpleefOcorrendo() {
		return this.spleefOcorrendo;
	}

	public void setSpleefOcorrendo(final Spleef spleefOcorrendo) {
		this.spleefOcorrendo = spleefOcorrendo;
	}

	public void reDownload() {
		Bukkit.getConsoleSender().sendMessage("§4[HEventos] §cPlugin sem licensa, desativado!");
		Bukkit.getPluginManager().disablePlugin(HEventos.getHEventos());
	}

}
