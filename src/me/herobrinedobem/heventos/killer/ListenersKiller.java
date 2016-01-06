package me.herobrinedobem.heventos.killer;

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

public class ListenersKiller implements Listener {

	@EventHandler
	private void onBlockBreakEvent(final BlockBreakEvent e) {
		if (HEventos.getHEventos().getEventosController().getKillerOcorrendo() != null) {
			final Player p = e.getPlayer();
			if (HEventos.getHEventos().getEventosController().getKillerOcorrendo().isAssistirAtivado()) {
				if (HEventos.getHEventos().getEventosController().getKillerOcorrendo().getCamarotePlayers().contains(p.getName())) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	private void onPlayerDamageEntityEvent(final EntityDamageByEntityEvent e) {
		if (HEventos.getHEventos().getEventosController().getKillerOcorrendo() != null) {
			if (e.getDamager() instanceof Player) {
				final Player p = (Player) e.getDamager();
				if (HEventos.getHEventos().getEventosController().getKillerOcorrendo().isPvp() == false) {
					if (HEventos.getHEventos().getEventosController().getKillerOcorrendo().getParticipantes().contains(p.getName())) {
						e.setCancelled(true);
					}
				}
				if (HEventos.getHEventos().getEventosController().getKillerOcorrendo().isAssistirAtivado()) {
					if (HEventos.getHEventos().getEventosController().getKillerOcorrendo().getCamarotePlayers().contains(p.getName())) {
						e.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler
	private void onPlayerDamageEvent(final EntityDamageEvent e) {
		if (HEventos.getHEventos().getEventosController().getKillerOcorrendo() != null) {
			if (e.getEntity() instanceof Player) {
				final Player p = (Player) e.getEntity();
				if (HEventos.getHEventos().getEventosController().getKillerOcorrendo().isPvp() == false) {
					if (HEventos.getHEventos().getEventosController().getKillerOcorrendo().getParticipantes().contains(p.getName())) {
						e.setCancelled(true);
					}
				}
				if (HEventos.getHEventos().getEventosController().getKillerOcorrendo().isAssistirAtivado()) {
					if (HEventos.getHEventos().getEventosController().getKillerOcorrendo().getCamarotePlayers().contains(p.getName())) {
						e.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler
	private void onPlayerDeathEvent(final PlayerDeathEvent e) {
		if (HEventos.getHEventos().getEventosController().getKillerOcorrendo() != null) {
			if (HEventos.getHEventos().getEventosController().getKillerOcorrendo().getParticipantes().contains(e.getEntity().getPlayer().getName())) {
				for (final String s : HEventos.getHEventos().getEventosController().getKillerOcorrendo().getParticipantes()) {
					final Player p = HEventos.getHEventos().getServer().getPlayer(s);
					p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgMorreu().replace("$player$", e.getEntity().getPlayer().getName()));
				}
				HEventos.getHEventos().getEventosController().getKillerOcorrendo().getParticipantes().remove(e.getEntity().getPlayer().getName());
				e.getEntity().getPlayer().teleport(HEventos.getHEventos().getEventosController().getKillerOcorrendo().getSaida());
				e.setNewTotalExp(e.getDroppedExp());
			}
			if (HEventos.getHEventos().getEventosController().getKillerOcorrendo().isAssistirAtivado()) {
				if (HEventos.getHEventos().getEventosController().getKillerOcorrendo().getCamarotePlayers().contains(e.getEntity().getPlayer().getName())) {
					HEventos.getHEventos().getEventosController().getKillerOcorrendo().getCamarotePlayers().remove(e.getEntity().getPlayer().getName());
					e.getEntity().getPlayer().teleport(HEventos.getHEventos().getEventosController().getKillerOcorrendo().getSaida());
					e.setNewTotalExp(e.getDroppedExp());
				}
			}
		}
	}

	@EventHandler
	private void onPlayerQuitEvent(final PlayerQuitEvent e) {
		if (HEventos.getHEventos().getEventosController().getKillerOcorrendo() != null) {
			if (HEventos.getHEventos().getEventosController().getKillerOcorrendo().getParticipantes().contains(e.getPlayer().getName())) {
				if (HEventos.getHEventos().getEventosController().getKillerOcorrendo().isAberto() == false) {
					e.getPlayer().setHealth(0);
				}
				e.getPlayer().teleport(HEventos.getHEventos().getEventosController().getKillerOcorrendo().getSaida());
				for (final String s : HEventos.getHEventos().getEventosController().getKillerOcorrendo().getParticipantes()) {
					final Player p = HEventos.getHEventos().getServer().getPlayer(s);
					p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgDesconect().replace("$player$", e.getPlayer().getName()));
				}
				HEventos.getHEventos().getEventosController().getKillerOcorrendo().getParticipantes().remove(e.getPlayer().getName());
			}
			if (HEventos.getHEventos().getEventosController().getKillerOcorrendo().isAssistirAtivado()) {
				if (HEventos.getHEventos().getEventosController().getKillerOcorrendo().getCamarotePlayers().contains(e.getPlayer().getName())) {
					HEventos.getHEventos().getEventosController().getKillerOcorrendo().getCamarotePlayers().remove(e.getPlayer().getName());
					e.getPlayer().teleport(HEventos.getHEventos().getEventosController().getKillerOcorrendo().getSaida());
				}
			}
		}
	}

	@EventHandler
	private void onPlayerProccessCommandEvent(final PlayerCommandPreprocessEvent e) {
		if (HEventos.getHEventos().getEventosController().getKillerOcorrendo() != null) {
			if (HEventos.getHEventos().getEventosController().getKillerOcorrendo().getParticipantes().contains(e.getPlayer().getName()) || HEventos.getHEventos().getEventosController().getKillerOcorrendo().getCamarotePlayers().contains(e.getPlayer().getName())) {
				if (!e.getPlayer().hasPermission("heventos.admin")) {
					for (final String s : HEventos.getHEventos().getEventosController().getKillerOcorrendo().getConfig().getStringList("Comandos_Liberados")) {
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
		if (HEventos.getHEventos().getEventosController().getKillerOcorrendo() != null) {
			if (HEventos.getHEventos().getEventosController().getKillerOcorrendo().isAssistirAtivado()) {
				if (HEventos.getHEventos().getEventosController().getKillerOcorrendo().getCamarotePlayers().contains(e.getPlayer().getName())) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	private void onBlockPlaceEvent(final BlockPlaceEvent e) {
		if (HEventos.getHEventos().getEventosController().getKillerOcorrendo() != null) {
			if (HEventos.getHEventos().getEventosController().getKillerOcorrendo().isAssistirAtivado()) {
				if (HEventos.getHEventos().getEventosController().getKillerOcorrendo().getCamarotePlayers().contains(e.getPlayer().getName())) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	private void onPlayerPickupEvent(final PlayerPickupItemEvent e) {
		if (HEventos.getHEventos().getEventosController().getKillerOcorrendo() != null) {
			final Player p = e.getPlayer();
			if (HEventos.getHEventos().getEventosController().getKillerOcorrendo().isAssistirAtivado()) {
				if (HEventos.getHEventos().getEventosController().getKillerOcorrendo().getCamarotePlayers().contains(p.getName())) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	private void onPotionSplashEvent(final PotionSplashEvent e) {
		if (HEventos.getHEventos().getEventosController().getKillerOcorrendo() != null) {
			if (HEventos.getHEventos().getEventosController().getKillerOcorrendo().isAssistirAtivado()) {
				e.setCancelled(true);
			}
		}
	}

}
