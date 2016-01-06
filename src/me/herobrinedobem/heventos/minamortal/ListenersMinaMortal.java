package me.herobrinedobem.heventos.minamortal;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import me.herobrinedobem.heventos.HEventos;

public class ListenersMinaMortal implements Listener {

	@EventHandler
	private void onBlockBreakEvent(final BlockBreakEvent e) {
		if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo() != null) {
			final Player p = e.getPlayer();
			if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().isAssistirAtivado()) {
				if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getCamarotePlayers().contains(p.getName())) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	private void onPlayerDamageEntityEvent(final EntityDamageByEntityEvent e) {
		if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo() != null) {
			if (e.getDamager() instanceof Player) {
				final Player p = (Player) e.getDamager();
				if (!HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().isPvp()) {
					if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getParticipantes().contains(p.getName())) {
						e.setCancelled(true);
					}
					if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().isAssistirAtivado()) {
						if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getCamarotePlayers().contains(p.getName())) {
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}

	@EventHandler
	private void onPlayerDamageEntityEvent(final EntityDamageEvent e) {
		if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo() != null) {
			if (e.getEntity() instanceof Player) {
				final Player p = (Player) e.getEntity();
				if (!HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().isPvp()) {
					if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getParticipantes().contains(p.getName())) {
						e.setCancelled(true);
					}
					if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().isAssistirAtivado()) {
						if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getCamarotePlayers().contains(p.getName())) {
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}

	@EventHandler
	private void onPlayerDeathEvent(final PlayerDeathEvent e) {
		if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo() != null) {
			if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getParticipantes().contains(e.getEntity().getPlayer().getName())) {
				for (final String s : HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getParticipantes()) {
					final Player p = HEventos.getHEventos().getServer().getPlayer(s);
					p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgMorreu().replace("$player$", e.getEntity().getPlayer().getName()));
				}
				HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getParticipantes().remove(e.getEntity().getPlayer().getName());
				e.getEntity().getPlayer().teleport(HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getSaida());
				e.setNewTotalExp(e.getDroppedExp());
			}
			if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().isAssistirAtivado()) {
				if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getCamarotePlayers().contains(e.getEntity().getPlayer().getName())) {
					HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getCamarotePlayers().remove(e.getEntity().getPlayer().getName());
					e.getEntity().getPlayer().teleport(HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getSaida());
					e.setNewTotalExp(e.getDroppedExp());
				}
			}
		}
	}

	@EventHandler
	private void onPlayerQuitEvent(final PlayerQuitEvent e) {
		if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo() != null) {
			if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getParticipantes().contains(e.getPlayer().getName())) {
				HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getParticipantes().remove(e.getPlayer().getName());
				e.getPlayer().teleport(HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getSaida());
				for (final String s : HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getParticipantes()) {
					final Player p = HEventos.getHEventos().getServer().getPlayer(s);
					p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgDesconect().replace("$player$", e.getPlayer().getName()));
				}
			}
			if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().isAssistirAtivado()) {
				if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getCamarotePlayers().contains(e.getPlayer().getName())) {
					HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getCamarotePlayers().remove(e.getPlayer().getName());
					e.getPlayer().teleport(HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getSaida());
				}
			}
		}
	}

	@EventHandler
	private void onPlayerProccessCommandEvent(final PlayerCommandPreprocessEvent e) {
		if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo() != null) {
			if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getParticipantes().contains(e.getPlayer().getName()) || HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getCamarotePlayers().contains(e.getPlayer().getName())) {
				if (!e.getPlayer().hasPermission("heventos.admin")) {
					for (final String s : HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getConfig().getStringList("Comandos_Liberados")) {
						if (!e.getMessage().startsWith(s)) {
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}

	@EventHandler
	private void onPlayerDropEvent(final PlayerDropItemEvent e) {
		if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo() != null) {
			if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().isAssistirAtivado()) {
				if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getCamarotePlayers().contains(e.getPlayer().getName())) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	private void onBlockPlaceEvent(final BlockPlaceEvent e) {
		if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo() != null) {
			if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().isAssistirAtivado()) {
				if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getCamarotePlayers().contains(e.getPlayer().getName())) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	private void onPlayerDamageEntityEvent(final PlayerPickupItemEvent e) {
		if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo() != null) {
			final Player p = e.getPlayer();
			if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().isAssistirAtivado()) {
				if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getCamarotePlayers().contains(p.getName())) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	private void onPotionSplashEvent(final PotionSplashEvent e) {
		if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo() != null) {
			if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().isAssistirAtivado()) {
				e.setCancelled(true);
			}
		}
	}

}
