package me.herobrinedobem.heventos.spleef;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import me.herobrinedobem.heventos.HEventos;

public class ListenersSpleef implements Listener {

	@EventHandler
	private void onBlockBreakEvent(final BlockBreakEvent e) {
		if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo() != null) {
			final Player p = e.getPlayer();
			if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getParticipantes().contains(p.getName())) {
				if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo().isPodeQuebrar() == false) {
					for (final String sa : HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getConfig().getStringList("Mensagens.Aguarde_Quebrar")) {
						e.getPlayer().sendMessage(sa.replace("&", "§").replace("$tempo$", HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getTempoComecarCurrent() + ""));
					}
					e.setCancelled(true);
				}
			}
			if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo().isAssistirAtivado()) {
				if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getCamarotePlayers().contains(p.getName())) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	private void onPlayerMoveEvent(final PlayerMoveEvent e) {
		if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo() != null) {
			final Player p = e.getPlayer();
			if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getParticipantes().contains(p.getName())) {
				if ((p.getLocation().getY() <= HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getY()) && (HEventos.getHEventos().getEventosController().getSpleefOcorrendo().isAberto() == false)) {
					e.getPlayer().teleport(HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getSaida());
					for (final String s : HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getParticipantes()) {
						final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
						for (final String sa : HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getConfig().getStringList("Mensagens.Eliminado")) {
							pa.sendMessage(sa.replace("&", "§").replace("$player$", e.getPlayer().getName()));
						}
					}
					HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getParticipantes().remove(e.getPlayer().getName());
				}
			}
		}
	}

	@EventHandler
	private void onPlayerDamageEntityEvent(final EntityDamageByEntityEvent e) {
		if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo() != null) {
			if (e.getDamager() instanceof Player) {
				final Player p = (Player) e.getDamager();
				if (!HEventos.getHEventos().getEventosController().getSpleefOcorrendo().isPvp()) {
					if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getParticipantes().contains(p.getName())) {
						e.setCancelled(true);
					}
					if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo().isAssistirAtivado()) {
						if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getCamarotePlayers().contains(p.getName())) {
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}

	@EventHandler
	private void onPlayerDamageEntityEvent(final EntityDamageEvent e) {
		if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo() != null) {
			if (e.getEntity() instanceof Player) {
				final Player p = (Player) e.getEntity();
				if (!HEventos.getHEventos().getEventosController().getSpleefOcorrendo().isPvp()) {
					if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getParticipantes().contains(p.getName())) {
						e.setCancelled(true);
					}
					if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo().isAssistirAtivado()) {
						if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getCamarotePlayers().contains(p.getName())) {
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}

	@EventHandler
	private void onPlayerDeathEvent(final PlayerDeathEvent e) {
		if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo() != null) {
			if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getParticipantes().contains(e.getEntity().getPlayer().getName())) {
				for (final String s : HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getParticipantes()) {
					final Player p = HEventos.getHEventos().getServer().getPlayer(s);
					p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgMorreu().replace("$player$", e.getEntity().getPlayer().getName()));
				}
				HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getParticipantes().remove(e.getEntity().getPlayer().getName());
				e.getEntity().getPlayer().teleport(HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getSaida());
				e.setNewTotalExp(e.getDroppedExp());
			}
			if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo().isAssistirAtivado()) {
				if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getCamarotePlayers().contains(e.getEntity().getPlayer().getName())) {
					HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getCamarotePlayers().remove(e.getEntity().getPlayer().getName());
					e.getEntity().getPlayer().teleport(HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getSaida());
					e.setNewTotalExp(e.getDroppedExp());
				}
			}
		}
	}

	@EventHandler
	private void onPlayerQuitEvent(final PlayerQuitEvent e) {
		if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo() != null) {
			if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getParticipantes().contains(e.getPlayer().getName())) {
				HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getParticipantes().remove(e.getPlayer().getName());
				e.getPlayer().teleport(HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getSaida());
				for (final String s : HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getParticipantes()) {
					final Player p = HEventos.getHEventos().getServer().getPlayer(s);
					p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgDesconect().replace("$player$", e.getPlayer().getName()));
				}
			}
			if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo().isAssistirAtivado()) {
				if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getCamarotePlayers().contains(e.getPlayer().getName())) {
					HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getCamarotePlayers().remove(e.getPlayer().getName());
					e.getPlayer().teleport(HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getSaida());
				}
			}
		}
	}

	@EventHandler
	private void onPlayerProccessCommandEvent(final PlayerCommandPreprocessEvent e) {
		if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo() != null) {
			if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getParticipantes().contains(e.getPlayer().getName()) || HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getCamarotePlayers().contains(e.getPlayer().getName())) {
				if (!e.getPlayer().hasPermission("heventos.admin")) {
					for (final String s : HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getConfig().getStringList("Comandos_Liberados")) {
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
		if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo() != null) {
			if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo().isAssistirAtivado()) {
				if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getCamarotePlayers().contains(e.getPlayer().getName())) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	private void onBlockPlaceEvent(final BlockPlaceEvent e) {
		if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo() != null) {
			if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getParticipantes().contains(e.getPlayer().getName())) {
				e.setCancelled(true);
			}
			if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo().isAssistirAtivado()) {
				if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getCamarotePlayers().contains(e.getPlayer().getName())) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	private void onPlayerDamageEntityEvent(final PlayerPickupItemEvent e) {
		if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo() != null) {
			final Player p = e.getPlayer();
			if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo().isAssistirAtivado()) {
				if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getCamarotePlayers().contains(p.getName())) {
					e.setCancelled(true);
				}
			}
		}
	}

}
