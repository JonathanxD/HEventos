package me.herobrinedobem.heventos.paintball;

import java.util.ArrayList;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitScheduler;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.utils.BukkitEventHelper;

public class Paintball {

	private ArrayList<String> participantes = new ArrayList<String>();
	private final ArrayList<String> team1 = new ArrayList<String>();
	private final ArrayList<String> team2 = new ArrayList<String>();
	private boolean ocorrendo, aberto, parte1, parte0, vip, assistirAtivado,
			assistirInvisivel, pvp, contarVitoria, contarParticipacao;
	private int chamadas, tempo, id, id2, chamadascurrent;
	private double money;
	private String nome;
	private Location saida, entrada, camarote, aguarde;
	private Location pos1;
	private Location pos2;
	private ArrayList<String> vencedores = new ArrayList<String>();
	private final ArrayList<String> camarotePlayers = new ArrayList<String>();
	private YamlConfiguration config;

	public Paintball(final YamlConfiguration config) {
		HEventos.getHEventos().getServer().getPluginManager().registerEvents(HEventos.getHEventos().getListeners(), HEventos.getHEventos());
		this.config = config;
		this.nome = config.getString("Config.Nome");
		this.chamadas = config.getInt("Config.Chamadas");
		if (this.vip == false) {
			this.vip = config.getBoolean("Config.VIP");
		}
		this.assistirAtivado = config.getBoolean("Config.Assistir_Ativado");
		this.assistirInvisivel = config.getBoolean("Config.Assistir_Invisivel");
		this.pvp = config.getBoolean("Config.PVP");
		this.contarParticipacao = config.getBoolean("Config.Contar_Participacao");
		this.contarVitoria = config.getBoolean("Config.Contar_Vitoria");
		this.tempo = config.getInt("Config.Tempo_Entre_As_Chamadas");
		this.saida = this.getLocation("Localizacoes.Saida");
		this.camarote = this.getLocation("Localizacoes.Camarote");
		this.entrada = this.getLocation("Localizacoes.Entrada");
		this.aguarde = this.getLocation("Localizacoes.Aguardando");
		this.pos1 = this.getLocation("Localizacoes.Pos_1");
		this.pos2 = this.getLocation("Localizacoes.Pos_2");
		this.money = config.getInt("Premios.Money") * HEventos.getHEventos().getConfig().getInt("Money_Multiplicador");
		this.aberto = false;
		this.ocorrendo = false;
		this.parte0 = false;
		this.parte1 = false;
		this.participantes.clear();
		this.team1.clear();
		this.team2.clear();
		this.chamadascurrent = this.chamadas;
	}

	public void start() {
		final BukkitScheduler scheduler = HEventos.getHEventos().getServer().getScheduler();
		this.id = scheduler.scheduleSyncRepeatingTask(HEventos.getHEventos(), new Runnable() {
			@Override
			public void run() {
				if (!Paintball.this.parte0) {
					if (Paintball.this.chamadascurrent >= 1) {
						Paintball.this.chamadascurrent--;
						Paintball.this.ocorrendo = true;
						Paintball.this.aberto = true;
						if (Paintball.this.vip) {
							Paintball.this.sendMessageList("Mensagens.Aberto_VIP");
						} else {
							Paintball.this.sendMessageList("Mensagens.Aberto");
						}
					} else if (Paintball.this.chamadascurrent == 0) {
						if (Paintball.this.participantes.size() >= 1) {
							Paintball.this.aberto = false;
							Paintball.this.parte1 = true;
							Paintball.this.parte0 = true;
							Paintball.this.sendMessageList("Mensagens.Iniciando");
							for (final String sa : Paintball.this.camarotePlayers) {
								Paintball.this.getPlayerByName(sa).teleport(Paintball.this.camarote);
							}
							boolean team1Ja = false;
							final ItemStack arco = new ItemStack(Material.BOW, 1);
							arco.addEnchantment(Enchantment.ARROW_INFINITE, 1);
							for (final String p : Paintball.this.participantes) {
								Paintball.this.getPlayerByName(p).getInventory().addItem(arco);
								Paintball.this.getPlayerByName(p).getInventory().addItem(new ItemStack(Material.ARROW, 64));
								if (team1Ja == false) {
									Paintball.this.team1.add(p);
									Paintball.this.darKitPaintball(Paintball.this.getPlayerByName(p), 2);
									Paintball.this.getPlayerByName(p).teleport(Paintball.this.pos1);
									team1Ja = true;
								} else {
									Paintball.this.team2.add(p);
									Paintball.this.darKitPaintball(Paintball.this.getPlayerByName(p), 1);
									Paintball.this.getPlayerByName(p).teleport(Paintball.this.pos2);
									team1Ja = false;
								}
								if (Paintball.this.contarParticipacao) {
									if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
										HEventos.getHEventos().getMysql().addPartipationPoint(p);
									} else {
										HEventos.getHEventos().getSqlite().addPartipationPoint(p);
									}
								}
							}
							for (final String s : Paintball.this.config.getStringList("Mensagens.Times")) {
								final StringBuilder time1Builder = new StringBuilder();
								for (final String p1 : Paintball.this.team1) {
									time1Builder.append("§6" + p1 + " ");
								}
								final StringBuilder time2Builder = new StringBuilder();
								for (final String p2 : Paintball.this.team2) {
									time2Builder.append("§6" + p2 + " ");
								}
								HEventos.getHEventos().getServer().broadcastMessage(s.replace("&", "§").replace("$time1$", time1Builder.toString()).replace("$time2$", time2Builder.toString()));
							}
						} else {
							Paintball.this.reset();
							Paintball.this.sendMessageList("Mensagens.Cancelado");
							HEventos.getHEventos().getServer().getScheduler().cancelTask(Paintball.this.id);
						}
					}
				}
			}
		}, 0, this.tempo * 20L);

		this.id2 = scheduler.scheduleSyncRepeatingTask(HEventos.getHEventos(), new Runnable() {
			@Override
			public void run() {
				if (Paintball.this.assistirInvisivel) {
					for (final String s : Paintball.this.participantes) {
						for (final String sa : Paintball.this.camarotePlayers) {
							Paintball.this.getPlayerByName(s).hidePlayer(Paintball.this.getPlayerByName(sa));
						}
					}
				}
				if ((Paintball.this.aberto == false) && (Paintball.this.ocorrendo == true)) {
					if (Paintball.this.participantes.size() > 0) {
						if (Paintball.this.team1.size() <= 0) {
							Paintball.this.encerrarEventoComVencedores(Paintball.this.team2);
						}
						if (Paintball.this.team2.size() <= 0) {
							Paintball.this.encerrarEventoComVencedores(Paintball.this.team1);
						}
					} else {
						Paintball.this.encerrarEventoSemVencedores();
					}
					Paintball.this.pos1.getWorld().setTime(17000);
				}
			}
		}, 0, 20L);
	}

	public void cancelarEvento() {
		for (final String s : this.participantes) {
			this.resetarInventario(this.getPlayerByName(s));
			this.getPlayerByName(s).teleport(this.getSaida());
		}
		for (final String s : this.camarotePlayers) {
			this.getPlayerByName(s).teleport(this.getSaida());
		}
		this.sendMessageList("Mensagens.Cancelado");
		this.reset();
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.id);
	}

	public void encerrarEventoSemVencedores() {
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.id);
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.id2);
		for (final String s : this.participantes) {
			this.resetarInventario(this.getPlayerByName(s));
			this.getPlayerByName(s).teleport(this.getSaida());
		}
		for (final String s : this.camarotePlayers) {
			this.getPlayerByName(s).teleport(this.getSaida());
		}
		this.sendMessageListVencedor("Mensagens.Sem_Vencedor");
		this.reset();
	}

	public void encerrarEventoComVencedores(final ArrayList<String> team) {
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.id);
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.id2);
		final StringBuilder time1Builder = new StringBuilder();
		for (final String p1 : team) {
			this.resetarInventario(this.getPlayerByName(p1));
			HEventos.getHEventos().getEconomy().depositPlayer(p1, this.money);
			time1Builder.append("§6" + p1 + " ");
		}
		for (final String s : this.config.getStringList("Mensagens.Vencedor")) {
			HEventos.getHEventos().getServer().broadcastMessage(s.replace("&", "§").replace("$players$", time1Builder.toString()));
		}
		for (final String s : this.participantes) {
			this.getPlayerByName(s).teleport(this.getSaida());
		}
		for (final String s : this.camarotePlayers) {
			this.getPlayerByName(s).teleport(this.getSaida());
		}
		this.reset();
	}

	private void reset() {
		this.nome = this.config.getString("Config.Nome");
		this.chamadas = this.config.getInt("Config.Chamadas");
		if (this.vip == false) {
			this.vip = this.config.getBoolean("Config.VIP");
		}
		this.assistirAtivado = this.config.getBoolean("Config.Assistir_Ativado");
		this.assistirInvisivel = this.config.getBoolean("Config.Assistir_Invisivel");
		this.pvp = this.config.getBoolean("Config.PVP");
		this.contarParticipacao = this.config.getBoolean("Config.Contar_Participacao");
		this.contarVitoria = this.config.getBoolean("Config.Contar_Vitoria");
		this.tempo = this.config.getInt("Config.Tempo_Entre_As_Chamadas");
		this.saida = this.getLocation("Localizacoes.Saida");
		this.camarote = this.getLocation("Localizacoes.Camarote");
		this.entrada = this.getLocation("Localizacoes.Entrada");
		this.aguarde = this.getLocation("Localizacoes.Aguardando");
		this.pos1 = this.getLocation("Localizacoes.Pos_1");
		this.pos2 = this.getLocation("Localizacoes.Pos_2");
		this.money = this.config.getInt("Premios.Money") * HEventos.getHEventos().getConfig().getInt("Money_Multiplicador");
		this.aberto = false;
		this.ocorrendo = false;
		this.parte0 = false;
		this.parte1 = false;
		this.participantes.clear();
		this.team1.clear();
		this.team2.clear();
		this.chamadascurrent = this.chamadas;
		for (final String s : this.camarotePlayers) {
			this.getPlayerByName(s).teleport(this.getSaida());
			this.getPlayerByName(s).setAllowFlight(false);
			this.getPlayerByName(s).setFlying(false);
		}
		for (final Player s : HEventos.getHEventos().getServer().getOnlinePlayers()) {
			for (final String sa : this.camarotePlayers) {
				s.showPlayer(this.getPlayerByName(sa));
			}
		}
		this.camarotePlayers.clear();
		HEventos.getHEventos().getEventosController().setPaintBallOcorrendo(null);
		BukkitEventHelper.unregisterEvents(HEventos.getHEventos().getListenersPaintball(), HEventos.getHEventos());
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.id);
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.id2);
	}

	private Player getPlayerByName(final String name) {
		return HEventos.getHEventos().getServer().getPlayer(name);
	}

	private void sendMessageList(final String list) {
		for (final String s : this.config.getStringList(list)) {
			HEventos.getHEventos().getServer().broadcastMessage(s.replace("&", "§"));
		}
	}

	private void sendMessageListVencedor(final String list) {
		for (final String s : this.config.getStringList(list)) {
			HEventos.getHEventos().getServer().broadcastMessage(s.replace("&", "§").replace("$player$", this.vencedores.get(0)));
		}
	}

	private void resetarInventario(final Player p) {
		p.getInventory().clear();
		p.getInventory().setHelmet(new ItemStack(Material.AIR));
		p.getInventory().setChestplate(new ItemStack(Material.AIR));
		p.getInventory().setLeggings(new ItemStack(Material.AIR));
		p.getInventory().setBoots(new ItemStack(Material.AIR));
	}

	private void darKitPaintball(final Player player, final int time) {
		if (time == 1) {
			final ItemStack lhelmet = new ItemStack(Material.LEATHER_HELMET, 1);
			final LeatherArmorMeta lam = (LeatherArmorMeta) lhelmet.getItemMeta();
			lam.setColor(Color.RED);
			lhelmet.setItemMeta(lam);

			final ItemStack lChest = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
			final LeatherArmorMeta lcm = (LeatherArmorMeta) lChest.getItemMeta();
			lcm.setColor(Color.RED);
			lChest.setItemMeta(lcm);

			final ItemStack lLegg = new ItemStack(Material.LEATHER_LEGGINGS, 1);
			final LeatherArmorMeta llg = (LeatherArmorMeta) lLegg.getItemMeta();
			llg.setColor(Color.RED);
			lLegg.setItemMeta(llg);

			final ItemStack lBoots = new ItemStack(Material.LEATHER_BOOTS, 1);
			final LeatherArmorMeta lbo = (LeatherArmorMeta) lBoots.getItemMeta();
			lbo.setColor(Color.RED);
			lBoots.setItemMeta(lbo);

			player.getInventory().setHelmet(lhelmet);
			player.getInventory().setChestplate(lChest);
			player.getInventory().setLeggings(lLegg);
			player.getInventory().setBoots(lBoots);

		} else if (time == 2) {
			final ItemStack lhelmet = new ItemStack(Material.LEATHER_HELMET, 1);
			final LeatherArmorMeta lam = (LeatherArmorMeta) lhelmet.getItemMeta();
			lam.setColor(Color.BLUE);
			lhelmet.setItemMeta(lam);

			final ItemStack lChest = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
			final LeatherArmorMeta lcm = (LeatherArmorMeta) lChest.getItemMeta();
			lcm.setColor(Color.BLUE);
			lChest.setItemMeta(lcm);

			final ItemStack lLegg = new ItemStack(Material.LEATHER_LEGGINGS, 1);
			final LeatherArmorMeta llg = (LeatherArmorMeta) lLegg.getItemMeta();
			llg.setColor(Color.BLUE);
			lLegg.setItemMeta(llg);

			final ItemStack lBoots = new ItemStack(Material.LEATHER_BOOTS, 1);
			final LeatherArmorMeta lbo = (LeatherArmorMeta) lBoots.getItemMeta();
			lbo.setColor(Color.BLUE);
			lBoots.setItemMeta(lbo);

			player.getInventory().setHelmet(lhelmet);
			player.getInventory().setChestplate(lChest);
			player.getInventory().setLeggings(lLegg);
			player.getInventory().setBoots(lBoots);
		}
	}

	private Location getLocation(final String local) {
		final String world = this.config.getString(local).split(";")[0];
		final double x = Double.parseDouble(this.config.getString(local).split(";")[1]);
		final double y = Double.parseDouble(this.config.getString(local).split(";")[2]);
		final double z = Double.parseDouble(this.config.getString(local).split(";")[3]);
		return new org.bukkit.Location(HEventos.getHEventos().getServer().getWorld(world), x, y, z);
	}

	public ArrayList<String> getCamarotePlayers() {
		return this.camarotePlayers;
	}

	public double getMoney() {
		return this.money;
	}

	public void setMoney(final double money) {
		this.money = money;
	}

	public YamlConfiguration getConfig() {
		return this.config;
	}

	public ArrayList<String> getParticipantes() {
		return this.participantes;
	}

	public void setParticipantes(final ArrayList<String> participantes) {
		this.participantes = participantes;
	}

	public boolean isOcorrendo() {
		return this.ocorrendo;
	}

	public void setOcorrendo(final boolean ocorrendo) {
		this.ocorrendo = ocorrendo;
	}

	public boolean isAberto() {
		return this.aberto;
	}

	public void setAberto(final boolean aberto) {
		this.aberto = aberto;
	}

	public int getChamadas() {
		return this.chamadas;
	}

	public void setChamadas(final int chamadas) {
		this.chamadas = chamadas;
	}

	public int getTempo() {
		return this.tempo;
	}

	public void setTempo(final int tempo) {
		this.tempo = tempo;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(final String nome) {
		this.nome = nome;
	}

	public Location getSaida() {
		return this.saida;
	}

	public void setSaida(final Location saida) {
		this.saida = saida;
	}

	public Location getEntrada() {
		return this.entrada;
	}

	public void setEntrada(final Location entrada) {
		this.entrada = entrada;
	}

	public Location getCamarote() {
		return this.camarote;
	}

	public void setCamarote(final Location camarote) {
		this.camarote = camarote;
	}

	public Location getAguarde() {
		return this.aguarde;
	}

	public void setAguarde(final Location aguarde) {
		this.aguarde = aguarde;
	}

	public boolean isParte1() {
		return this.parte1;
	}

	public void setParte1(final boolean parte1) {
		this.parte1 = parte1;
	}

	public ArrayList<String> getVencedores() {
		return this.vencedores;
	}

	public void setVencedores(final ArrayList<String> vencedores) {
		this.vencedores = vencedores;
	}

	public boolean isParte0() {
		return this.parte0;
	}

	public void setParte0(final boolean parte0) {
		this.parte0 = parte0;
	}

	public boolean isVip() {
		return this.vip;
	}

	public void setVip(final boolean vip) {
		this.vip = vip;
	}

	public boolean isAssistirAtivado() {
		return this.assistirAtivado;
	}

	public void setAssistirAtivado(final boolean assistirAtivado) {
		this.assistirAtivado = assistirAtivado;
	}

	public boolean isAssistirInvisivel() {
		return this.assistirInvisivel;
	}

	public void setAssistirInvisivel(final boolean assistirInvisivel) {
		this.assistirInvisivel = assistirInvisivel;
	}

	public boolean isPvp() {
		return this.pvp;
	}

	public void setPvp(final boolean pvp) {
		this.pvp = pvp;
	}

	public boolean isContarVitoria() {
		return this.contarVitoria;
	}

	public void setContarVitoria(final boolean contarVitoria) {
		this.contarVitoria = contarVitoria;
	}

	public boolean isContarParticipacao() {
		return this.contarParticipacao;
	}

	public void setContarParticipacao(final boolean contarParticipacao) {
		this.contarParticipacao = contarParticipacao;
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public int getId2() {
		return this.id2;
	}

	public void setId2(final int id2) {
		this.id2 = id2;
	}

	public int getChamadascurrent() {
		return this.chamadascurrent;
	}

	public void setChamadascurrent(final int chamadascurrent) {
		this.chamadascurrent = chamadascurrent;
	}

	public void setConfig(final YamlConfiguration config) {
		this.config = config;
	}

	public Location getPos1() {
		return this.pos1;
	}

	public void setPos1(final Location pos1) {
		this.pos1 = pos1;
	}

	public Location getPos2() {
		return this.pos2;
	}

	public void setPos2(final Location pos2) {
		this.pos2 = pos2;
	}

	public ArrayList<String> getTeam1() {
		return this.team1;
	}

	public ArrayList<String> getTeam2() {
		return this.team2;
	}

}
