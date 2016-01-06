package me.herobrinedobem.heventos;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import me.herobrinedobem.heventos.batataquente.BatataQuente;
import me.herobrinedobem.heventos.eventos.Evento;
import me.herobrinedobem.heventos.frog.Frog;
import me.herobrinedobem.heventos.killer.Killer;
import me.herobrinedobem.heventos.minamortal.MinaMortal;
import me.herobrinedobem.heventos.paintball.Paintball;
import me.herobrinedobem.heventos.spleef.Spleef;

public class Comandos implements CommandExecutor {

	private final HEventos instance;

	public Comandos(final HEventos instance) {
		this.instance = instance;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
		Player p;
		if (sender instanceof Player) {
			p = (Player) sender;

			if (cmd.getName().equalsIgnoreCase("evento")) {
				if ((args.length == 1) && (args[0].equalsIgnoreCase("entrar"))) {
					if (HEventos.getHEventos().getEventosController().getEventoOcorrendo() != null) {
						if (!HEventos.getHEventos().getEventosController().getEventoOcorrendo().getParticipantes().contains(p.getName())) {
							if (HEventos.getHEventos().getEventosController().getEventoOcorrendo().isAberto()) {
								if (HEventos.getHEventos().getEventosController().getEventoOcorrendo().isVip()) {
									if (p.hasPermission("heventos.vip") || p.hasPermission("heventos.admin")) {
										HEventos.getHEventos().getEventosController().getEventoOcorrendo().getParticipantes().add(p.getName());
										p.teleport(HEventos.getHEventos().getEventosController().getEventoOcorrendo().getAguarde());
										for (final String s : HEventos.getHEventos().getEventosController().getEventoOcorrendo().getParticipantes()) {
											final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
											pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgEntrou().replace("$player$", p.getName()));
										}
										return true;
									} else {
										p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgEventoVip());
										return true;
									}
								} else {
									HEventos.getHEventos().getEventosController().getEventoOcorrendo().getParticipantes().add(p.getName());
									p.teleport(HEventos.getHEventos().getEventosController().getEventoOcorrendo().getAguarde());
									for (final String s : HEventos.getHEventos().getEventosController().getEventoOcorrendo().getParticipantes()) {
										final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
										pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgEntrou().replace("$player$", p.getName()));
									}
									return true;
								}
							} else {
								p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgEventoFechado());
								return true;
							}
						} else {
							p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgJaParticipa());
							return true;
						}
					} else if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo() != null) {
						if (!HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getParticipantes().contains(p.getName())) {
							if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().isAberto()) {
								if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().isVip()) {
									if (p.hasPermission("heventos.vip") || p.hasPermission("heventos.admin")) {
										HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getParticipantes().add(p.getName());
										p.teleport(HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getAguarde());
										for (final String s : HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getParticipantes()) {
											final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
											pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgEntrou().replace("$player$", p.getName()));
										}
										return true;
									} else {
										p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgEventoVip());
										return true;
									}
								} else {
									HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getParticipantes().add(p.getName());
									p.teleport(HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getAguarde());
									for (final String s : HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getParticipantes()) {
										final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
										pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgEntrou().replace("$player$", p.getName()));
									}
									return true;
								}
							} else {
								p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgEventoFechado());
								return true;
							}
						} else {
							p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgJaParticipa());
							return true;
						}
					} else if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo() != null) {
						if (!HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getParticipantes().contains(p.getName())) {
							if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().isAberto()) {
								if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().isVip()) {
									if (p.hasPermission("heventos.vip") || p.hasPermission("heventos.admin")) {
										HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getParticipantes().add(p.getName());
										p.teleport(HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getAguarde());
										for (final String s : HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getParticipantes()) {
											final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
											pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgEntrou().replace("$player$", p.getName()));
										}
										return true;
									} else {
										p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgEventoVip());
										return true;
									}
								} else {
									HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getParticipantes().add(p.getName());
									p.teleport(HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getAguarde());
									for (final String s : HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getParticipantes()) {
										final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
										pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgEntrou().replace("$player$", p.getName()));
									}
									return true;
								}
							} else {
								p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgEventoFechado());
								return true;
							}
						} else {
							p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgJaParticipa());
							return true;
						}
					} else if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo() != null) {
						if (!HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getParticipantes().contains(p.getName())) {
							if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo().isAberto()) {
								if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo().isVip()) {
									if (p.hasPermission("heventos.vip") || p.hasPermission("heventos.admin")) {
										HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getParticipantes().add(p.getName());
										p.teleport(HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getAguarde());
										for (final String s : HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getParticipantes()) {
											final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
											pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgEntrou().replace("$player$", p.getName()));
										}
										return true;
									} else {
										p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgEventoVip());
										return true;
									}
								} else {
									HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getParticipantes().add(p.getName());
									p.teleport(HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getAguarde());
									for (final String s : HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getParticipantes()) {
										final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
										pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgEntrou().replace("$player$", p.getName()));
									}
									return true;
								}
							} else {
								p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgEventoFechado());
								return true;
							}
						} else {
							p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgJaParticipa());
							return true;
						}
					} else if (HEventos.getHEventos().getEventosController().getFrogOcorrendo() != null) {
						if (!HEventos.getHEventos().getEventosController().getFrogOcorrendo().getParticipantes().contains(p.getName())) {
							if (HEventos.getHEventos().getEventosController().getFrogOcorrendo().isAberto()) {
								HEventos.getHEventos().getEventosController().getFrogOcorrendo().getParticipantes().add(p.getName());
								p.teleport(HEventos.getHEventos().getEventosController().getFrogOcorrendo().getAguarde());
								for (final String s : HEventos.getHEventos().getEventosController().getFrogOcorrendo().getParticipantes()) {
									final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
									pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgEntrou().replace("$player$", p.getName()));
								}
								return true;
							} else {
								p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgEventoFechado());
								return true;
							}
						} else {
							p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgJaParticipa());
							return true;
						}
					} else if (HEventos.getHEventos().getEventosController().getKillerOcorrendo() != null) {
						if (!HEventos.getHEventos().getEventosController().getKillerOcorrendo().getParticipantes().contains(p.getName())) {
							if (HEventos.getHEventos().getEventosController().getKillerOcorrendo().isAberto()) {
								if (HEventos.getHEventos().getEventosController().getKillerOcorrendo().isVip()) {
									if (p.hasPermission("heventos.vip") || p.hasPermission("heventos.admin")) {
										if (HEventos.getHEventos().getSc().getClanManager().getClanPlayer(p.getName()) != null) {
											HEventos.getHEventos().getSc().getClanManager().getClanPlayer(p.getName()).setFriendlyFire(true);
										}
										HEventos.getHEventos().getServer().dispatchCommand(p, "clan ff allow");
										HEventos.getHEventos().getEventosController().getKillerOcorrendo().getParticipantes().add(p.getName());
										p.teleport(HEventos.getHEventos().getEventosController().getKillerOcorrendo().getAguarde());
										for (final String s : HEventos.getHEventos().getEventosController().getKillerOcorrendo().getParticipantes()) {
											final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
											pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgEntrou().replace("$player$", p.getName()));
										}
										return true;
									} else {
										p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgEventoVip());
										return true;
									}
								} else {
									HEventos.getHEventos().getEventosController().getKillerOcorrendo().getParticipantes().add(p.getName());
									p.teleport(HEventos.getHEventos().getEventosController().getKillerOcorrendo().getAguarde());
									for (final String s : HEventos.getHEventos().getEventosController().getKillerOcorrendo().getParticipantes()) {
										final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
										pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgEntrou().replace("$player$", p.getName()));
									}
									return true;
								}
							} else {
								p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgEventoFechado());
								return true;
							}
						} else {
							p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgJaParticipa());
							return true;
						}
					} else if (HEventos.getHEventos().getEventosController().getPaintballOcorrendo() != null) {
						if (!HEventos.getHEventos().getEventosController().getPaintballOcorrendo().getParticipantes().contains(p.getName())) {
							if (HEventos.getHEventos().getEventosController().getPaintballOcorrendo().isAberto()) {
								if (HEventos.getHEventos().getEventosController().getPaintballOcorrendo().isVip()) {
									if (p.hasPermission("heventos.vip") || p.hasPermission("heventos.admin")) {
										if (Comandos.isInventoryEmpty(p)) {
											HEventos.getHEventos().getEventosController().getPaintballOcorrendo().getParticipantes().add(p.getName());
											p.teleport(HEventos.getHEventos().getEventosController().getPaintballOcorrendo().getAguarde());
											for (final String s : HEventos.getHEventos().getEventosController().getPaintballOcorrendo().getParticipantes()) {
												final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
												pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgEntrou().replace("$player$", p.getName()));
											}
											return true;
										} else {
											p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgInventarioVazio());
											return true;
										}
									} else {
										p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgEventoVip());
										return true;
									}
								} else {
									if (Comandos.isInventoryEmpty(p)) {
										HEventos.getHEventos().getEventosController().getPaintballOcorrendo().getParticipantes().add(p.getName());
										p.teleport(HEventos.getHEventos().getEventosController().getPaintballOcorrendo().getAguarde());
										for (final String s : HEventos.getHEventos().getEventosController().getPaintballOcorrendo().getParticipantes()) {
											final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
											pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgEntrou().replace("$player$", p.getName()));
										}
										return true;
									} else {
										p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgInventarioVazio());
										return true;
									}
								}
							} else {
								p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgEventoFechado());
								return true;
							}
						} else {
							p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgJaParticipa());
							return true;
						}
					}
				} else if ((args.length == 1) && (args[0].equalsIgnoreCase("sair"))) {
					if (HEventos.getHEventos().getEventosController().getEventoOcorrendo() != null) {
						if (HEventos.getHEventos().getEventosController().getEventoOcorrendo().getParticipantes().contains(p.getName())) {
							for (final String s : HEventos.getHEventos().getEventosController().getEventoOcorrendo().getParticipantes()) {
								final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
								pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgSaiu().replace("$player$", p.getName()));
							}
							HEventos.getHEventos().getEventosController().getEventoOcorrendo().getParticipantes().remove(p.getName());
							p.teleport(HEventos.getHEventos().getEventosController().getEventoOcorrendo().getSaida());
							return true;
						} else {
							if (HEventos.getHEventos().getEventosController().getEventoOcorrendo().getCamarotePlayers().contains(p.getName())) {
								p.teleport(HEventos.getHEventos().getEventosController().getEventoOcorrendo().getSaida());
								p.setAllowFlight(false);
								p.setFlying(false);
								for (final String s : HEventos.getHEventos().getEventosController().getEventoOcorrendo().getParticipantes()) {
									final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
									pa.showPlayer(p);
								}
								HEventos.getHEventos().getEventosController().getEventoOcorrendo().getCamarotePlayers().remove(p.getName());
								for (final String s : HEventos.getHEventos().getEventosController().getEventoOcorrendo().getParticipantes()) {
									final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
									pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgSaiu().replace("$player$", p.getName()));
								}
								p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgSaiu().replace("$player$", p.getName()));
							} else {
								p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgNaoParticipa());
								return true;
							}
						}
					} else if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo() != null) {
						if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo() != null) {
							if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getParticipantes().contains(p.getName())) {
								for (final String s : HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getParticipantes()) {
									final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
									pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgSaiu().replace("$player$", p.getName()));
								}
								HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getParticipantes().remove(p.getName());
								p.teleport(HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getSaida());
								return true;
							} else {
								if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getCamarotePlayers().contains(p.getName())) {
									p.teleport(HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getSaida());
									p.setAllowFlight(false);
									p.setFlying(false);
									for (final String s : HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getParticipantes()) {
										final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
										pa.showPlayer(p);
									}
									HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getCamarotePlayers().remove(p.getName());
									for (final String s : HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getParticipantes()) {
										final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
										pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgSaiu().replace("$player$", p.getName()));
									}
									p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgSaiu().replace("$player$", p.getName()));
								} else {
									p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgNaoParticipa());
									return true;
								}
							}
						}
					} else if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo() != null) {
						if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo() != null) {
							if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getParticipantes().contains(p.getName())) {
								for (final String s : HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getParticipantes()) {
									final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
									pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgSaiu().replace("$player$", p.getName()));
								}
								HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getParticipantes().remove(p.getName());
								p.teleport(HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getSaida());
								return true;
							} else {
								if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getCamarotePlayers().contains(p.getName())) {
									p.teleport(HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getSaida());
									p.setAllowFlight(false);
									p.setFlying(false);
									for (final String s : HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getParticipantes()) {
										final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
										pa.showPlayer(p);
									}
									HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getCamarotePlayers().remove(p.getName());
									for (final String s : HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getParticipantes()) {
										final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
										pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgSaiu().replace("$player$", p.getName()));
									}
									p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgSaiu().replace("$player$", p.getName()));
								} else {
									p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgNaoParticipa());
									return true;
								}
							}
						}
					} else if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo() != null) {
						if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo() != null) {
							if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getParticipantes().contains(p.getName())) {
								for (final String s : HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getParticipantes()) {
									final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
									pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgSaiu().replace("$player$", p.getName()));
								}
								HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getParticipantes().remove(p.getName());
								p.teleport(HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getSaida());
								return true;
							} else {
								if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getCamarotePlayers().contains(p.getName())) {
									p.teleport(HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getSaida());
									p.setAllowFlight(false);
									p.setFlying(false);
									for (final String s : HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getParticipantes()) {
										final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
										pa.showPlayer(p);
									}
									HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getCamarotePlayers().remove(p.getName());
									for (final String s : HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getParticipantes()) {
										final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
										pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgSaiu().replace("$player$", p.getName()));
									}
									p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgSaiu().replace("$player$", p.getName()));
								} else {
									p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgNaoParticipa());
									return true;
								}
							}
						}
					} else if (HEventos.getHEventos().getEventosController().getFrogOcorrendo() != null) {
						if (HEventos.getHEventos().getEventosController().getFrogOcorrendo() != null) {
							if (HEventos.getHEventos().getEventosController().getFrogOcorrendo().getParticipantes().contains(p.getName())) {
								for (final String s : HEventos.getHEventos().getEventosController().getFrogOcorrendo().getParticipantes()) {
									final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
									pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgSaiu().replace("$player$", p.getName()));
								}
								HEventos.getHEventos().getEventosController().getFrogOcorrendo().getParticipantes().remove(p.getName());
								p.teleport(HEventos.getHEventos().getEventosController().getFrogOcorrendo().getSaida());
								return true;
							} else {
								if (HEventos.getHEventos().getEventosController().getFrogOcorrendo().getCamarotePlayers().contains(p.getName())) {
									p.teleport(HEventos.getHEventos().getEventosController().getFrogOcorrendo().getSaida());
									p.setAllowFlight(false);
									p.setFlying(false);
									for (final String s : HEventos.getHEventos().getEventosController().getFrogOcorrendo().getParticipantes()) {
										final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
										pa.showPlayer(p);
									}
									HEventos.getHEventos().getEventosController().getFrogOcorrendo().getCamarotePlayers().remove(p.getName());
									for (final String s : HEventos.getHEventos().getEventosController().getFrogOcorrendo().getParticipantes()) {
										final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
										pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgSaiu().replace("$player$", p.getName()));
									}
									p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgSaiu().replace("$player$", p.getName()));
								} else {
									p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgNaoParticipa());
									return true;
								}
							}
						}
					} else if (HEventos.getHEventos().getEventosController().getKillerOcorrendo() != null) {
						if (HEventos.getHEventos().getEventosController().getKillerOcorrendo() != null) {
							if (HEventos.getHEventos().getEventosController().getKillerOcorrendo().getParticipantes().contains(p.getName())) {
								for (final String s : HEventos.getHEventos().getEventosController().getKillerOcorrendo().getParticipantes()) {
									final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
									pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgSaiu().replace("$player$", p.getName()));
								}
								if (HEventos.getHEventos().getEventosController().getKillerOcorrendo().isAberto() == false) {
									p.setHealth(0);
								}
								HEventos.getHEventos().getEventosController().getKillerOcorrendo().getParticipantes().remove(p.getName());
								p.teleport(HEventos.getHEventos().getEventosController().getKillerOcorrendo().getSaida());
								return true;
							} else {
								if (HEventos.getHEventos().getEventosController().getKillerOcorrendo().getCamarotePlayers().contains(p.getName())) {
									p.teleport(HEventos.getHEventos().getEventosController().getKillerOcorrendo().getSaida());
									p.setAllowFlight(false);
									p.setFlying(false);
									for (final String s : HEventos.getHEventos().getEventosController().getKillerOcorrendo().getParticipantes()) {
										final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
										pa.showPlayer(p);
									}
									HEventos.getHEventos().getEventosController().getKillerOcorrendo().getCamarotePlayers().remove(p.getName());
									for (final String s : HEventos.getHEventos().getEventosController().getKillerOcorrendo().getParticipantes()) {
										final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
										pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgSaiu().replace("$player$", p.getName()));
									}
									p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgSaiu().replace("$player$", p.getName()));
								} else {
									p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgNaoParticipa());
									return true;
								}
							}
						}
					} else if (HEventos.getHEventos().getEventosController().getPaintballOcorrendo() != null) {
						if (HEventos.getHEventos().getEventosController().getPaintballOcorrendo() != null) {
							if (HEventos.getHEventos().getEventosController().getPaintballOcorrendo().getParticipantes().contains(p.getName())) {
								for (final String s : HEventos.getHEventos().getEventosController().getPaintballOcorrendo().getParticipantes()) {
									final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
									pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgSaiu().replace("$player$", p.getName()));
								}
								if (HEventos.getHEventos().getEventosController().getPaintballOcorrendo().isAberto() == false) {
									p.setHealth(0);
								}
								HEventos.getHEventos().getEventosController().getPaintballOcorrendo().getParticipantes().remove(p.getName());
								p.teleport(HEventos.getHEventos().getEventosController().getPaintballOcorrendo().getSaida());
								return true;
							} else {
								if (HEventos.getHEventos().getEventosController().getPaintballOcorrendo().getCamarotePlayers().contains(p.getName())) {
									p.teleport(HEventos.getHEventos().getEventosController().getPaintballOcorrendo().getSaida());
									p.setAllowFlight(false);
									p.setFlying(false);
									for (final String s : HEventos.getHEventos().getEventosController().getPaintballOcorrendo().getParticipantes()) {
										final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
										pa.showPlayer(p);
									}
									HEventos.getHEventos().getEventosController().getPaintballOcorrendo().getCamarotePlayers().remove(p.getName());
									for (final String s : HEventos.getHEventos().getEventosController().getPaintballOcorrendo().getParticipantes()) {
										final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
										pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgSaiu().replace("$player$", p.getName()));
									}
									p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgSaiu().replace("$player$", p.getName()));
								} else {
									p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgNaoParticipa());
									return true;
								}
							}
						}
					} else {
						p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgNenhumEvento());
						return true;
					}
				} else if ((args.length == 3) && (args[0].equalsIgnoreCase("iniciar"))) {
					if (p.hasPermission("heventos.admin")) {
						if ((HEventos.getHEventos().getEventosController().getEventoOcorrendo() == null) && (HEventos.getHEventos().getEventosController().getPaintballOcorrendo() == null) && (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo() == null) && (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo() == null) && (HEventos.getHEventos().getEventosController().getSpleefOcorrendo() == null) && (HEventos.getHEventos().getEventosController().getKillerOcorrendo() == null) && (HEventos.getHEventos().getEventosController().getFrogOcorrendo() == null)) {
							if (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("false")) {
								if (args[1].equalsIgnoreCase("batataquente")) {
									if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo() == null) {
										HEventos.getHEventos().getServer().getPluginManager().registerEvents(HEventos.getHEventos().getListenersBatataQuente(), HEventos.getHEventos());
										final File fileEvento = new File(HEventos.getHEventos().getDataFolder().getAbsolutePath() + "/Eventos/batataquente.yml");
										final YamlConfiguration configEvento = YamlConfiguration.loadConfiguration(fileEvento);
										HEventos.getHEventos().getEventosController().setBatataQuenteOcorrendo(new BatataQuente(configEvento));
										HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().setVip(Boolean.parseBoolean(args[2]));
										HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().start();
										p.sendMessage("§4[Evento] §cEvento iniciado com sucesso!");
										return true;
									}
								} else if (args[1].equalsIgnoreCase("minamortal")) {
									if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo() == null) {
										HEventos.getHEventos().getServer().getPluginManager().registerEvents(HEventos.getHEventos().getListenersMinaMortal(), HEventos.getHEventos());
										final File fileEvento = new File(HEventos.getHEventos().getDataFolder().getAbsolutePath() + "/Eventos/minamortal.yml");
										final YamlConfiguration configEvento = YamlConfiguration.loadConfiguration(fileEvento);
										HEventos.getHEventos().getEventosController().setMinaMortalOcorrendo(new MinaMortal(configEvento));
										HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().setVip(Boolean.parseBoolean(args[2]));
										HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().start();
										p.sendMessage("§4[Evento] §cEvento iniciado com sucesso!");
										return true;
									}
								} else if (args[1].equalsIgnoreCase("spleef")) {
									if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo() == null) {
										HEventos.getHEventos().getServer().getPluginManager().registerEvents(HEventos.getHEventos().getListenersSpleef(), HEventos.getHEventos());
										final File fileEvento = new File(HEventos.getHEventos().getDataFolder().getAbsolutePath() + "/Eventos/spleef.yml");
										final YamlConfiguration configEvento = YamlConfiguration.loadConfiguration(fileEvento);
										HEventos.getHEventos().getEventosController().setSpleefOcorrendo(new Spleef(configEvento));
										HEventos.getHEventos().getEventosController().getSpleefOcorrendo().setVip(Boolean.parseBoolean(args[2]));
										HEventos.getHEventos().getEventosController().getSpleefOcorrendo().start();
										p.sendMessage("§4[Evento] §cEvento iniciado com sucesso!");
										return true;
									}
								} else if (args[1].equalsIgnoreCase("frog")) {
									if (HEventos.getHEventos().getEventosController().getFrogOcorrendo() == null) {
										HEventos.getHEventos().getServer().getPluginManager().registerEvents(HEventos.getHEventos().getListenersFrog(), HEventos.getHEventos());
										final File fileEvento = new File(HEventos.getHEventos().getDataFolder().getAbsolutePath() + "/Eventos/frog.yml");
										final YamlConfiguration configEvento = YamlConfiguration.loadConfiguration(fileEvento);
										HEventos.getHEventos().getEventosController().setFrogOcorrendo(new Frog(configEvento));
										// HEventos.getHEventos().getEventosController().getFrogOcorrendo().setVip(Boolean.parseBoolean(args[2]));
										HEventos.getHEventos().getEventosController().getFrogOcorrendo().start();
										p.sendMessage("§4[Evento] §cEvento iniciado com sucesso!");
										return true;
									}
								} else if (args[1].equalsIgnoreCase("killer")) {
									if (HEventos.getHEventos().getEventosController().getKillerOcorrendo() == null) {
										HEventos.getHEventos().getServer().getPluginManager().registerEvents(HEventos.getHEventos().getListenersKiller(), HEventos.getHEventos());
										final File fileEvento = new File(HEventos.getHEventos().getDataFolder().getAbsolutePath() + "/Eventos/killer.yml");
										final YamlConfiguration configEvento = YamlConfiguration.loadConfiguration(fileEvento);
										HEventos.getHEventos().getEventosController().setKillerOcorrendo(new Killer(configEvento));
										HEventos.getHEventos().getEventosController().getKillerOcorrendo().setVip(Boolean.parseBoolean(args[2]));
										HEventos.getHEventos().getEventosController().getKillerOcorrendo().start();
										p.sendMessage("§4[Evento] §cEvento iniciado com sucesso!");
										return true;
									}
								} else if (args[1].equalsIgnoreCase("paintball")) {
									if (HEventos.getHEventos().getEventosController().getPaintballOcorrendo() == null) {
										HEventos.getHEventos().getServer().getPluginManager().registerEvents(HEventos.getHEventos().getListenersPaintball(), HEventos.getHEventos());
										final File fileEvento = new File(HEventos.getHEventos().getDataFolder().getAbsolutePath() + "/Eventos/paintball.yml");
										final YamlConfiguration configEvento = YamlConfiguration.loadConfiguration(fileEvento);
										HEventos.getHEventos().getEventosController().setPaintBallOcorrendo(new Paintball(configEvento));
										HEventos.getHEventos().getEventosController().getPaintballOcorrendo().setVip(Boolean.parseBoolean(args[2]));
										HEventos.getHEventos().getEventosController().getPaintballOcorrendo().start();
										p.sendMessage("§4[Evento] §cEvento iniciado com sucesso!");
										return true;
									}
								} else if (HEventos.getHEventos().getEventosController().hasEvento(args[1])) {
									HEventos.getHEventos().getEventosController().setEventoOcorrendo(HEventos.getHEventos().getEventosController().loadEvento(args[1]));
									HEventos.getHEventos().getEventosController().getEventoOcorrendo().setVip(Boolean.parseBoolean(args[2]));
									HEventos.getHEventos().getEventosController().getEventoOcorrendo().start();
									p.sendMessage("§4[Evento] §cEvento iniciado com sucesso!");
									return true;
								} else {
									p.sendMessage("§4[Evento] §cEvento nao encontrado!");
									return true;
								}
							} else {
								p.sendMessage("§4[Evento] §cUtilize /evento iniciar <nome> <true/false>");
								return true;
							}
						} else {
							p.sendMessage("§4[Evento] §cJa existe um evento ocorrendo no momento!");
							return true;
						}
					}
				} else if ((args.length == 2) && (args[0].equalsIgnoreCase("iniciar"))) {
					p.sendMessage("§4[Evento] §cUtilize /evento iniciar <nome> <true/false>");
					return true;
				} else if ((args.length == 1) && (args[0].equalsIgnoreCase("cancelar"))) {
					if (p.hasPermission("heventos.admin")) {
						if (HEventos.getHEventos().getEventosController().getEventoOcorrendo() != null) {
							HEventos.getHEventos().getEventosController().getEventoOcorrendo().cancelarEvento();
							p.sendMessage("§4[Evento] §cEvento cancelado com sucesso!");
							return true;
						} else if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo() != null) {
							HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().cancelarEvento();
							p.sendMessage("§4[Evento] §cEvento cancelado com sucesso!");
							return true;
						} else if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo() != null) {
							HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().cancelarEvento();
							p.sendMessage("§4[Evento] §cEvento cancelado com sucesso!");
							return true;
						} else if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo() != null) {
							HEventos.getHEventos().getEventosController().getSpleefOcorrendo().cancelarEvento();
							p.sendMessage("§4[Evento] §cEvento cancelado com sucesso!");
							return true;
						} else if (HEventos.getHEventos().getEventosController().getFrogOcorrendo() != null) {
							HEventos.getHEventos().getEventosController().getFrogOcorrendo().cancelarEvento();
							p.sendMessage("§4[Evento] §cEvento cancelado com sucesso!");
							return true;
						} else if (HEventos.getHEventos().getEventosController().getKillerOcorrendo() != null) {
							HEventos.getHEventos().getEventosController().getKillerOcorrendo().cancelarEvento();
							p.sendMessage("§4[Evento] §cEvento cancelado com sucesso!");
							return true;
						} else if (HEventos.getHEventos().getEventosController().getPaintballOcorrendo() != null) {
							HEventos.getHEventos().getEventosController().getPaintballOcorrendo().cancelarEvento();
							p.sendMessage("§4[Evento] §cEvento cancelado com sucesso!");
							return true;
						} else {
							p.sendMessage("§4[Evento] §cNao existe um evento ocorrendo no momento!");
							return true;
						}
					}
				} else if ((args.length == 1) && (args[0].equalsIgnoreCase("iniciar"))) {
					if (p.hasPermission("heventos.admin")) {
						p.sendMessage("§4[Evento] §cUtilize /evento iniciar <nome> <true/false>");
						return true;
					}
				} else if ((args.length == 1) && (args[0].equalsIgnoreCase("assistir"))) {
					if (HEventos.getHEventos().getEventosController().getEventoOcorrendo() != null) {
						if (HEventos.getHEventos().getEventosController().getEventoOcorrendo().isAssistirAtivado()) {
							if (!HEventos.getHEventos().getEventosController().getEventoOcorrendo().getParticipantes().contains(p.getName())) {
								if (!HEventos.getHEventos().getEventosController().getEventoOcorrendo().getCamarotePlayers().contains(p.getName())) {
									HEventos.getHEventos().getEventosController().getEventoOcorrendo().getCamarotePlayers().add(p.getName());
									if (HEventos.getHEventos().getEventosController().getEventoOcorrendo().isAberto()) {
										p.teleport(HEventos.getHEventos().getEventosController().getEventoOcorrendo().getAguarde());
									} else {
										p.teleport(HEventos.getHEventos().getEventosController().getEventoOcorrendo().getEntrada());
									}
									for (final String s : HEventos.getHEventos().getEventosController().getEventoOcorrendo().getParticipantes()) {
										final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
										pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgAssistindo().replace("$player$", p.getName()));
									}
									for (final String s : HEventos.getHEventos().getEventosController().getEventoOcorrendo().getCamarotePlayers()) {
										final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
										pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgAssistindo().replace("$player$", p.getName()));
									}
									p.setAllowFlight(true);
									p.setFlying(true);
									p.setFlySpeed(1 / 10.0f);
								} else {
									p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgJaEstaCamarote());
									return true;
								}
							} else {
								p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgJaParticipa());
								return true;
							}
						} else {
							p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgAssistirDesativado());
							return true;
						}
					} else if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo() != null) {
						if (!HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getParticipantes().contains(p.getName())) {
							if (!HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getCamarotePlayers().contains(p.getName())) {
								HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getCamarotePlayers().add(p.getName());
								if (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().isAberto()) {
									p.teleport(HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getAguarde());
								} else {
									p.teleport(HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getEntrada());
								}
								for (final String s : HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getParticipantes()) {
									final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
									pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgAssistindo().replace("$player$", p.getName()));
								}
								for (final String s : HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo().getCamarotePlayers()) {
									final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
									pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgAssistindo().replace("$player$", p.getName()));
								}
								p.setAllowFlight(true);
								p.setFlying(true);
								p.setFlySpeed(1 / 10.0f);
							} else {
								p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgJaEstaCamarote());
								return true;
							}
						} else {
							p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgJaParticipa());
							return true;
						}
					} else if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo() != null) {
						if (!HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getParticipantes().contains(p.getName())) {
							if (!HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getCamarotePlayers().contains(p.getName())) {
								HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getCamarotePlayers().add(p.getName());
								if (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().isAberto()) {
									p.teleport(HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getAguarde());
								} else {
									p.teleport(HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getEntrada());
								}
								for (final String s : HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getParticipantes()) {
									final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
									pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgAssistindo().replace("$player$", p.getName()));
								}
								for (final String s : HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo().getCamarotePlayers()) {
									final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
									pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgAssistindo().replace("$player$", p.getName()));
								}
								p.setAllowFlight(true);
								p.setFlying(true);
								p.setFlySpeed(1 / 10.0f);
							} else {
								p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgJaEstaCamarote());
								return true;
							}
						} else {
							p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgJaParticipa());
							return true;
						}
					} else if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo() != null) {
						if (!HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getParticipantes().contains(p.getName())) {
							if (!HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getCamarotePlayers().contains(p.getName())) {
								HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getCamarotePlayers().add(p.getName());
								if (HEventos.getHEventos().getEventosController().getSpleefOcorrendo().isAberto()) {
									p.teleport(HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getAguarde());
								} else {
									p.teleport(HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getEntrada());
								}
								for (final String s : HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getParticipantes()) {
									final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
									pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgAssistindo().replace("$player$", p.getName()));
								}
								for (final String s : HEventos.getHEventos().getEventosController().getSpleefOcorrendo().getCamarotePlayers()) {
									final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
									pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgAssistindo().replace("$player$", p.getName()));
								}
								p.setAllowFlight(true);
								p.setFlying(true);
								p.setFlySpeed(1 / 10.0f);
							} else {
								p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgJaEstaCamarote());
								return true;
							}
						} else {
							p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgJaParticipa());
							return true;
						}
					} else if (HEventos.getHEventos().getEventosController().getFrogOcorrendo() != null) {
						if (!HEventos.getHEventos().getEventosController().getFrogOcorrendo().getParticipantes().contains(p.getName())) {
							if (!HEventos.getHEventos().getEventosController().getFrogOcorrendo().getCamarotePlayers().contains(p.getName())) {
								HEventos.getHEventos().getEventosController().getFrogOcorrendo().getCamarotePlayers().add(p.getName());
								if (HEventos.getHEventos().getEventosController().getFrogOcorrendo().isAberto()) {
									p.teleport(HEventos.getHEventos().getEventosController().getFrogOcorrendo().getAguarde());
								} else {
									p.teleport(HEventos.getHEventos().getEventosController().getFrogOcorrendo().getEntrada());
								}
								for (final String s : HEventos.getHEventos().getEventosController().getFrogOcorrendo().getParticipantes()) {
									final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
									pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgAssistindo().replace("$player$", p.getName()));
								}
								for (final String s : HEventos.getHEventos().getEventosController().getFrogOcorrendo().getCamarotePlayers()) {
									final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
									pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgAssistindo().replace("$player$", p.getName()));
								}
								p.setAllowFlight(true);
								p.setFlying(true);
								p.setFlySpeed(1 / 10.0f);
							} else {
								p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgJaEstaCamarote());
								return true;
							}
						} else {
							p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgJaParticipa());
							return true;
						}
					} else if (HEventos.getHEventos().getEventosController().getKillerOcorrendo() != null) {
						if (!HEventos.getHEventos().getEventosController().getKillerOcorrendo().getParticipantes().contains(p.getName())) {
							if (!HEventos.getHEventos().getEventosController().getKillerOcorrendo().getCamarotePlayers().contains(p.getName())) {
								HEventos.getHEventos().getEventosController().getKillerOcorrendo().getCamarotePlayers().add(p.getName());
								if (HEventos.getHEventos().getEventosController().getKillerOcorrendo().isAberto()) {
									p.teleport(HEventos.getHEventos().getEventosController().getKillerOcorrendo().getAguarde());
								} else {
									p.teleport(HEventos.getHEventos().getEventosController().getKillerOcorrendo().getEntrada());
								}
								for (final String s : HEventos.getHEventos().getEventosController().getKillerOcorrendo().getParticipantes()) {
									final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
									pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgAssistindo().replace("$player$", p.getName()));
								}
								for (final String s : HEventos.getHEventos().getEventosController().getKillerOcorrendo().getCamarotePlayers()) {
									final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
									pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgAssistindo().replace("$player$", p.getName()));
								}
								p.setAllowFlight(true);
								p.setFlying(true);
								p.setFlySpeed(1 / 10.0f);
							} else {
								p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgJaEstaCamarote());
								return true;
							}
						} else {
							p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgJaParticipa());
							return true;
						}
					} else if (HEventos.getHEventos().getEventosController().getPaintballOcorrendo() != null) {
						if (!HEventos.getHEventos().getEventosController().getPaintballOcorrendo().getParticipantes().contains(p.getName())) {
							if (!HEventos.getHEventos().getEventosController().getPaintballOcorrendo().getCamarotePlayers().contains(p.getName())) {
								HEventos.getHEventos().getEventosController().getPaintballOcorrendo().getCamarotePlayers().add(p.getName());
								if (HEventos.getHEventos().getEventosController().getPaintballOcorrendo().isAberto()) {
									p.teleport(HEventos.getHEventos().getEventosController().getPaintballOcorrendo().getAguarde());
								} else {
									p.teleport(HEventos.getHEventos().getEventosController().getPaintballOcorrendo().getEntrada());
								}
								for (final String s : HEventos.getHEventos().getEventosController().getPaintballOcorrendo().getParticipantes()) {
									final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
									pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgAssistindo().replace("$player$", p.getName()));
								}
								for (final String s : HEventos.getHEventos().getEventosController().getPaintballOcorrendo().getCamarotePlayers()) {
									final Player pa = HEventos.getHEventos().getServer().getPlayer(s);
									pa.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgAssistindo().replace("$player$", p.getName()));
								}
								p.setAllowFlight(true);
								p.setFlying(true);
								p.setFlySpeed(1 / 10.0f);
							} else {
								p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgJaEstaCamarote());
								return true;
							}
						} else {
							p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgJaParticipa());
							return true;
						}
					} else {
						p.sendMessage(HEventos.getHEventos().getConfigUtil().getMsgNenhumEvento());
						return true;
					}
				} else if ((args.length == 1) && (args[0].equalsIgnoreCase("multiplicador"))) {
					if (p.hasPermission("heventos.admin")) {
						p.sendMessage("§4[Evento] §cUtilize /evento multiplicador <valor>");
					}
				} else if ((args.length == 2) && (args[0].equalsIgnoreCase("multiplicador"))) {
					if (p.hasPermission("heventos.admin")) {
						try {
							this.instance.getConfig().set("Money_Multiplicador", Integer.parseInt(args[1]));
							p.sendMessage("§4[Evento] §cRate alterado com sucesso!");
							HEventos.getHEventos().getServer().broadcastMessage("§4[Eventos] §cMultiplicador de money dos eventos alterado para §4" + args[1] + "*");
							return true;
						} catch (final NumberFormatException e) {
							p.sendMessage("§4[Evento] §cUtilize apenas numeros no rate.");
							return true;
						}
					}
				} else if ((args.length == 2) && (args[0].equalsIgnoreCase("multiplicador")) && (args[1].equalsIgnoreCase("reset"))) {
					if (p.hasPermission("heventos.admin")) {
						this.instance.getConfig().set("Money_Multiplicador", 1);
						p.sendMessage("§4[Evento] §cRate alterado com sucesso!");
						HEventos.getHEventos().getServer().broadcastMessage("  ");
						HEventos.getHEventos().getServer().broadcastMessage("§4[Eventos] §cRate de money dos eventos voltou ao normal!");
						HEventos.getHEventos().getServer().broadcastMessage("  ");
						return true;
					}
				} else if ((args.length == 1) && (args[0].equalsIgnoreCase("topvencedores"))) {
					if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
						HEventos.getHEventos().getMysql().getTOPWins(p);
					} else {
						HEventos.getHEventos().getSqlite().getTOPWins(p);
					}
				} else if ((args.length == 1) && (args[0].equalsIgnoreCase("topparticipacoes"))) {
					if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
						HEventos.getHEventos().getMysql().getTOPParticipations(p);
					} else {
						HEventos.getHEventos().getSqlite().getTOPParticipations(p);
					}
				} else if ((args.length == 2) && (args[0].equalsIgnoreCase("setentrada"))) {
					if (p.hasPermission("heventos.admin")) {
						if (HEventos.getHEventos().getEventosController().hasEvento(args[1])) {
							final Evento evento = HEventos.getHEventos().getEventosController().loadEvento(args[1]);
							evento.getConfig().set("Localizacoes.Entrada", this.getLocationForConfig(p.getLocation()));
							final File file = new File(HEventos.getHEventos().getDataFolder() + File.separator + "Eventos" + File.separator + args[1] + ".yml");
							try {
								evento.getConfig().save(file);
							} catch (final IOException e) {
								e.printStackTrace();
							}
							p.sendMessage("§4[Evento] §cEntrada do evento " + args[1] + " setada com sucesso!");
						} else {
							p.sendMessage("§4[Evento] §cEvento nao encontrado na pasta.");
							return true;
						}
					}
				} else if ((args.length == 2) && (args[0].equalsIgnoreCase("setsaida"))) {
					if (p.hasPermission("heventos.admin")) {
						if (HEventos.getHEventos().getEventosController().hasEvento(args[1])) {
							final Evento evento = HEventos.getHEventos().getEventosController().loadEvento(args[1]);
							evento.getConfig().set("Localizacoes.Saida", this.getLocationForConfig(p.getLocation()));
							final File file = new File(HEventos.getHEventos().getDataFolder() + File.separator + "Eventos" + File.separator + args[1] + ".yml");
							try {
								evento.getConfig().save(file);
							} catch (final IOException e) {
								e.printStackTrace();
							}
							p.sendMessage("§4[Evento] §cSaida do evento " + args[1] + " setada com sucesso!");
						} else {
							p.sendMessage("§4[Evento] §cEvento nao encontrado na pasta.");
							return true;
						}
					}
				} else if ((args.length == 2) && (args[0].equalsIgnoreCase("setcamarote"))) {
					if (p.hasPermission("heventos.admin")) {
						if (HEventos.getHEventos().getEventosController().hasEvento(args[1])) {
							final Evento evento = HEventos.getHEventos().getEventosController().loadEvento(args[1]);
							evento.getConfig().set("Localizacoes.Camarote", this.getLocationForConfig(p.getLocation()));
							final File file = new File(HEventos.getHEventos().getDataFolder() + File.separator + "Eventos" + File.separator + args[1] + ".yml");
							try {
								evento.getConfig().save(file);
							} catch (final IOException e) {
								e.printStackTrace();
							}
							p.sendMessage("§4[Evento] §cCamarote do evento " + args[1] + " setado com sucesso!");
						} else {
							p.sendMessage("§4[Evento] §cEvento nao encontrado na pasta.");
							return true;
						}
					}
				} else if ((args.length == 2) && (args[0].equalsIgnoreCase("setaguardando"))) {
					if (p.hasPermission("heventos.admin")) {
						if (HEventos.getHEventos().getEventosController().hasEvento(args[1])) {
							final Evento evento = HEventos.getHEventos().getEventosController().loadEvento(args[1]);
							evento.getConfig().set("Localizacoes.Aguardando", this.getLocationForConfig(p.getLocation()));
							final File file = new File(HEventos.getHEventos().getDataFolder() + File.separator + "Eventos" + File.separator + args[1] + ".yml");
							try {
								evento.getConfig().save(file);
							} catch (final IOException e) {
								e.printStackTrace();
							}
							p.sendMessage("§4[Evento] §cLocal de espera do evento " + args[1] + " setado com sucesso!");
						} else {
							p.sendMessage("§4[Evento] §cEvento nao encontrado na pasta.");
							return true;
						}
					}
				} else if ((args.length == 1) && (args[0].equalsIgnoreCase("reload"))) {
					if (p.hasPermission("heventos.admin")) {
						HEventos.getHEventos().reloadConfig();
						HEventos.getHEventos().getConfigUtil().setupConfigUtil();
						p.sendMessage("§4[Evento] §cConfiguraçao recarregada com sucesso!");
						return true;
					}
				} else if ((args.length == 1) && (args[0].equalsIgnoreCase("lista"))) {
					if (p.hasPermission("heventos.admin")) {
						final StringBuilder builder = new StringBuilder();
						for (final File file : new File(this.instance.getDataFolder().getAbsolutePath() + "/Eventos/").listFiles()) {
							builder.append("§6" + file.getName().replace(".yml", "") + " §0- ");
						}
						p.sendMessage("§4[Evento] §cLista de eventos:");
						p.sendMessage(builder.toString());
						return true;
					}
				} else if ((args.length == 2) && (args[0].equalsIgnoreCase("tool"))) {
					if (p.hasPermission("heventos.admin")) {
						if (args[1].equalsIgnoreCase("spleef")) {
							final ItemStack item = new ItemStack(Material.IRON_AXE, 1);
							final ItemMeta meta = item.getItemMeta();
							meta.setDisplayName("§4§lEvento Spleef");
							meta.setLore(Arrays.asList("§6* Clique com o botao direito para marcar a posicao 1 do chao", "§6* Clique com o botao esquerdo para marcar a posicao 2 do chao"));
							item.setItemMeta(meta);
							p.getInventory().addItem(item);
							p.updateInventory();
							return true;
						} else if (args[1].equalsIgnoreCase("minamortal")) {
							final ItemStack item = new ItemStack(Material.IRON_AXE, 1);
							final ItemMeta meta = item.getItemMeta();
							meta.setDisplayName("§4§lEvento MinaMortal");
							meta.setLore(Arrays.asList("§6* Clique com o botao direito para marcar a posicao 1 da mina", "§6* Clique com o botao esquerdo para marcar a posicao 2 da mina"));
							item.setItemMeta(meta);
							p.getInventory().addItem(item);
							p.updateInventory();
							return true;
						} else {
							p.sendMessage("§4[Evento] §cUtilize /evento tool <spleef/minamortal>");
							return true;
						}
					}
				} else {
					for (final String s : HEventos.getHEventos().getConfig().getStringList("Mensagens.Default")) {
						p.sendMessage(s.replace("&", "§"));
					}
					if (p.hasPermission("heventos.admin")) {
						p.sendMessage("§4/evento iniciar <nome> §c- Inicia um evento");
						p.sendMessage("§4/evento cancelar §c- Cancela um evento");
						p.sendMessage("§4/evento setentrada <evento> §c- Seta a entrada de um evento");
						p.sendMessage("§4/evento setsaida <evento> §c- Seta a saida de um evento");
						p.sendMessage("§4/evento setcamarote <evento> §c- Seta o camarote de um evento");
						p.sendMessage("§4/evento setaguardando <evento> §c- Seta local de espera de um evento");
						p.sendMessage("§4/evento multiplicador §c- Altera o multiplicador de money");
						p.sendMessage("§4/evento multiplicador reset §c- Reseta o multiplicador");
						p.sendMessage("§4/evento reload §c- Recarrega a config do plugin");
						p.sendMessage("§4/evento tool <evento> §c- Pega uma ferramenta para setar locs");
						p.sendMessage("§4/evento lista §c- Mostra a lista de eventos");
					}
				}

			}

		}
		return true;

	}

	private String getLocationForConfig(final Location loc) {
		final String world = loc.getWorld().getName();
		final int x = (int) loc.getX();
		final int y = (int) loc.getY();
		final int z = (int) loc.getZ();
		return world + ";" + String.valueOf(x) + ";" + String.valueOf(y) + ";" + String.valueOf(z);
	}

	private static boolean isInventoryEmpty(final Player p) {
		for (final ItemStack item : p.getInventory().getContents()) {
			if (item != null) {
				return false;
			}
		}
		if (p.getInventory().getHelmet() != null) {
			if ((p.getInventory().getHelmet().getType() != Material.AIR)) {
				return false;
			}
		}
		if (p.getInventory().getChestplate() != null) {
			if ((p.getInventory().getChestplate().getType() != Material.AIR)) {
				return false;
			}
		}
		if (p.getInventory().getLeggings() != null) {
			if ((p.getInventory().getLeggings().getType() != Material.AIR)) {
				return false;
			}
		}
		if (p.getInventory().getBoots() != null) {
			if ((p.getInventory().getBoots().getType() != Material.AIR)) {
				return false;
			}
		}
		return true;
	}

}
