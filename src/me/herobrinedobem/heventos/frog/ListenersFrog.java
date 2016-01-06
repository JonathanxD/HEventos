package me.herobrinedobem.heventos.frog;

import org.bukkit.Location;
import org.bukkit.Material;
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

public class ListenersFrog implements Listener {

	@EventHandler
	private void onBlockBreakEvent(final BlockBreakEvent e) {
		if (HEventos.getHEventos().getEventosController().getFrogOcorrendo() != null) {
			final Player p = e.getPlayer();
			if (HEventos.getHEventos().getEventosController().getFrogOcorrendo().getCamarotePlayers().contains(p.getName())) {
				e.setCancelled(true);
			} else if (HEventos.getHEventos().getEventosController().getFrogOcorrendo().getParticipantes().contains(p.getName())) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	private void onPlayerMoveEvent(final PlayerMoveEvent e) {
		if (HEventos.getHEventos().getEventosController().getFrogOcorrendo() != null) {
			final Player p = e.getPlayer();
			if (HEventos.getHEventos().getEventosController().getFrogOcorrendo().getParticipantes().contains(p.getName())) {
				if ((p.getLocation().getY() <= HEventos.getHEventos().getEventosController().getFrogOcorrendo().getY()) && (HEventos.getHEventos().getEventosController().getFrogOcorrendo().isAberto() == false)) {
					e.getPlayer().teleport(HEventos.getHEventos().getEventosController().getFrogOcorrendo().getSaida());
					for (final String s : HEventos.getHEventos().getEventosController().getFrogOcorrendo().getParticipantes()) {
						final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
						for (final String sa : HEventos.getHEventos().getEventosController().getFrogOcorrendo().getConfig().getStringList("Mensagens.Eliminado")) {
							pa.sendMessage(sa.replace("&", "§").replace("$player$", e.getPlayer().getName()));
						}
					}
					HEventos.getHEventos().getEventosController().getFrogOcorrendo().getParticipantes().remove(e.getPlayer().getName());
				} else if (HEventos.getHEventos().getEventosController().getFrogOcorrendo().isRedstoneSpawnada()) {
					final Location l = new Location(e.getTo().getWorld(), e.getTo().getX(), e.getTo().getY() - 1, e.getTo().getZ());
					if (l.getBlock().getType() == Material.REDSTONE_BLOCK) {
						HEventos.getHEventos().getEventosController().getFrogOcorrendo().encerrarEventoComVencedor(e.getPlayer());
					}
				}
			}
		}
	}

	@EventHandler
	private void onPlayerDamageEntityEvent(final EntityDamageByEntityEvent e) {
		if (HEventos.getHEventos().getEventosController().getFrogOcorrendo() != null) {
			if (e.getDamager() instanceof Player) {
				final Player p = (Player) e.getDamager();
				if (HEventos.getHEventos().getEventosController().getFrogOcorrendo().getCamarotePlayers().contains(p.getName())) {
					e.setCancelled(true);
				} else if (HEventos.getHEventos().getEventosController().getFrogOcorrendo().getParticipantes().contains(p.getName())) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	private void onPlayerDamageEntityEvent(final EntityDamageEvent e) {
		if (HEventos.getHEventos().getEventosController().getFrogOcorrendo() != null) {
			if (e.getEntity() instanceof Player) {
				final Player p = (Player) e.getEntity();
				if (HEventos.getHEventos().getEventosController().getFrogOcorrendo().getCamarotePlayers().contains(p.getName())) {
					e.setCancelled(true);
				} else if (HEventos.getHEventos().getEventosController().getFrogOcorrendo().getParticipantes().contains(p.getName())) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	private void onPlayerDeathEvent(final PlayerDeathEvent e) {
		if (HEventos.getHEventos().getEventosController().getFrogOcorrendo() != null) {
			if (HEventos.getHEventos().getEventosController().getFrogOcorrendo().getParticipantes().contains(e.getEntity().getPlayer().getName())) {
				for (final String s : HEventos.getHEventos().getEventosController().getFrogOcorrendo().getParticipantes()) {
					final Player p = HEventos.getHEventos().getServer().getPlayer(s);
					p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgMorreu().replace("$player$", e.getEntity().getPlayer().getName()));
				}
				HEventos.getHEventos().getEventosController().getFrogOcorrendo().getParticipantes().remove(e.getEntity().getPlayer().getName());
				e.getEntity().getPlayer().teleport(HEventos.getHEventos().getEventosController().getFrogOcorrendo().getSaida());
				e.setNewTotalExp(e.getDroppedExp());
			} else if (HEventos.getHEventos().getEventosController().getFrogOcorrendo().getCamarotePlayers().contains(e.getEntity().getPlayer().getName())) {
				HEventos.getHEventos().getEventosController().getFrogOcorrendo().getCamarotePlayers().remove(e.getEntity().getPlayer().getName());
				e.getEntity().getPlayer().teleport(HEventos.getHEventos().getEventosController().getFrogOcorrendo().getSaida());
				e.setNewTotalExp(e.getDroppedExp());
			}
		}
	}

	@EventHandler
	private void onPlayerQuitEvent(final PlayerQuitEvent e) {
		if (HEventos.getHEventos().getEventosController().getFrogOcorrendo() != null) {
			if (HEventos.getHEventos().getEventosController().getFrogOcorrendo().getParticipantes().contains(e.getPlayer().getName())) {
				HEventos.getHEventos().getEventosController().getFrogOcorrendo().getParticipantes().remove(e.getPlayer().getName());
				e.getPlayer().teleport(HEventos.getHEventos().getEventosController().getFrogOcorrendo().getSaida());
				for (final String s : HEventos.getHEventos().getEventosController().getFrogOcorrendo().getParticipantes()) {
					final Player p = HEventos.getHEventos().getServer().getPlayer(s);
					p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgDesconect().replace("$player$", e.getPlayer().getName()));
				}
			} else if (HEventos.getHEventos().getEventosController().getFrogOcorrendo().getCamarotePlayers().contains(e.getPlayer().getName())) {
				HEventos.getHEventos().getEventosController().getFrogOcorrendo().getCamarotePlayers().remove(e.getPlayer().getName());
				e.getPlayer().teleport(HEventos.getHEventos().getEventosController().getFrogOcorrendo().getSaida());
			}
		}
	}

	@EventHandler
	private void onPlayerProccessCommandEvent(final PlayerCommandPreprocessEvent e) {
		if (HEventos.getHEventos().getEventosController().getFrogOcorrendo() != null) {
			if (HEventos.getHEventos().getEventosController().getFrogOcorrendo().getParticipantes().contains(e.getPlayer().getName()) || HEventos.getHEventos().getEventosController().getFrogOcorrendo().getCamarotePlayers().contains(e.getPlayer().getName())) {
				if (!e.getPlayer().hasPermission("heventos.admin")) {
					for (final String s : HEventos.getHEventos().getEventosController().getFrogOcorrendo().getConfig().getStringList("Comandos_Liberados")) {
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
		if (HEventos.getHEventos().getEventosController().getFrogOcorrendo() != null) {
			if (HEventos.getHEventos().getEventosController().getFrogOcorrendo().getCamarotePlayers().contains(e.getPlayer().getName())) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	private void onBlockPlaceEvent(final BlockPlaceEvent e) {
		if (HEventos.getHEventos().getEventosController().getFrogOcorrendo() != null) {
			if (HEventos.getHEventos().getEventosController().getFrogOcorrendo().getCamarotePlayers().contains(e.getPlayer().getName())) {
				e.setCancelled(true);
			} else if (HEventos.getHEventos().getEventosController().getFrogOcorrendo().getParticipantes().contains(e.getPlayer().getName())) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	private void onPlayerDamageEntityEvent(final PlayerPickupItemEvent e) {
		if (HEventos.getHEventos().getEventosController().getFrogOcorrendo() != null) {
			final Player p = e.getPlayer();
			if (HEventos.getHEventos().getEventosController().getFrogOcorrendo().getCamarotePlayers().contains(p.getName())) {
				e.setCancelled(true);
			}
		}
	}

}
