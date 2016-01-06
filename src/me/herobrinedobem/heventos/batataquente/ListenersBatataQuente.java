package me.herobrinedobem.heventos.batataquente;

import java.util.Random;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import me.herobrinedobem.heventos.HEventos;

public class ListenersBatataQuente implements Listener {

	@EventHandler
	private void onPlayerInteractEvent(final PlayerInteractEntityEvent e) {
		if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo() != null) {
			if (e.getRightClicked() instanceof Player) {
				final Player pa = (Player) e.getRightClicked();
				if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().isAberto() == false) {
					if (e.getPlayer().getName().equalsIgnoreCase(HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getPlayerComBatata().getName())) {
						if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getParticipantes().contains(pa.getName())) {
							HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().setPlayerComBatata((Player) e.getRightClicked());
							for (final String s : HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getConfig().getStringList("Mensagens.Esta_Com_Batata")) {
								for (final String sa : HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getParticipantes()) {
									final Player p = HEventos.getHEventos().getServer().getPlayer(sa);
									p.sendMessage(s.replace("&", "§").replace("$player$", HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getPlayerComBatata().getName()));
								}
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	private void onPlayerDamageEntityEvent(final EntityDamageByEntityEvent e) {
		if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo() != null) {
			if (e.getDamager() instanceof Player) {
				final Player p = (Player) e.getDamager();
				if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().isPvp() == false) {
					if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().isAberto() == false) {
						if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getParticipantes().contains(p.getName())) {
							e.setCancelled(true);
						}
					}
					if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().isAssistirAtivado()) {
						if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getCamarotePlayers().contains(p.getName())) {
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}

	@EventHandler
	private void onPlayerDamageEntityEvent(final EntityDamageEvent e) {
		if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo() != null) {
			if (e.getEntity() instanceof Player) {
				final Player p = (Player) e.getEntity();
				if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().isPvp() == false) {
					if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().isAberto() == false) {
						if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getParticipantes().contains(p.getName())) {
							e.setCancelled(true);
						}
					}
					if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().isAssistirAtivado()) {
						if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getCamarotePlayers().contains(p.getName())) {
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}

	@EventHandler
	private void onPlayerPickupEvent(final PlayerPickupItemEvent e) {
		if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo() != null) {
			if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().isAssistirAtivado()) {
				final Player p = e.getPlayer();
				if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().isAberto() == false) {
					if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getCamarotePlayers().contains(p.getName())) {
						e.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler
	private void onPlayerDeathEvent(final PlayerDeathEvent e) {
		if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo() != null) {
			if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().isAberto() == false) {
				if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getParticipantes().contains(e.getEntity().getPlayer().getName())) {
					for (final String s : HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getParticipantes()) {
						final Player p = HEventos.getHEventos().getServer().getPlayer(s);
						p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgMorreu().replace("$player$", e.getEntity().getPlayer().getName()));
					}
					HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getParticipantes().remove(e.getEntity().getPlayer().getName());
					e.getEntity().getPlayer().teleport(HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getSaida());
					e.setNewTotalExp(e.getDroppedExp());
				}
				if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().isAssistirAtivado()) {
					if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getCamarotePlayers().contains(e.getEntity().getPlayer().getName())) {
						HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getCamarotePlayers().remove(e.getEntity().getPlayer().getName());
						e.getEntity().getPlayer().teleport(HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getSaida());
						e.setNewTotalExp(e.getDroppedExp());
					}
				}
			}
		}
	}

	@EventHandler
	private void onPlayerQuitEvent(final PlayerQuitEvent e) {
		if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo() != null) {
			if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getParticipantes().contains(e.getPlayer().getName())) {
				HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getParticipantes().remove(e.getPlayer().getName());
				e.getPlayer().teleport(HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getSaida());
				for (final String s : HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getParticipantes()) {
					final Player p = HEventos.getHEventos().getServer().getPlayer(s);
					p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgDesconect().replace("$player$", e.getPlayer().getName()));
				}
				if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getPlayerComBatata() == e.getPlayer()) {
					final Random r = new Random();
					HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().setPlayerComBatata(this.getPlayerByName(HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getParticipantes().get(r.nextInt(HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getParticipantes().size()))));
					for (final String s : HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getConfig().getStringList("Mensagens.Esta_Com_Batata")) {
						for (final String sa : HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getParticipantes()) {
							final Player sab = this.getPlayerByName(sa);
							sab.sendMessage(s.replace("&", "§").replace("$player$", HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getPlayerComBatata().getName()));
						}
					}
				}
			}
			if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().isAssistirAtivado()) {
				if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getCamarotePlayers().contains(e.getPlayer().getName())) {
					HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getCamarotePlayers().remove(e.getPlayer().getName());
					e.getPlayer().teleport(HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getSaida());
				}
			}
		}
	}

	@EventHandler
	private void onPlayerProccessCommandEvent(final PlayerCommandPreprocessEvent e) {
		if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo() != null) {
			if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getParticipantes().contains(e.getPlayer().getName()) || HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getCamarotePlayers().contains(e.getPlayer().getName())) {
				if (!e.getPlayer().hasPermission("heventos.admin")) {
					for (final String s : HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getConfig().getStringList("Comandos_Liberados")) {
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
		if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo() != null) {
			if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().isAssistirAtivado()) {
				if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().isAberto() == false) {
					if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getCamarotePlayers().contains(e.getPlayer().getName())) {
						e.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler
	private void onBlockBreakEvent(final BlockBreakEvent e) {
		if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo() != null) {
			if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().isAssistirAtivado()) {
				final Player p = e.getPlayer();
				if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().isAberto() == false) {
					if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getCamarotePlayers().contains(p.getName())) {
						e.setCancelled(true);
					}
				}
			}
		}
	}

	private Player getPlayerByName(final String name) {
		return HEventos.getHEventos().getServer().getPlayer(name);
	}

}
