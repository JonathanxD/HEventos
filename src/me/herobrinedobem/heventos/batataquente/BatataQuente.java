package me.herobrinedobem.heventos.batataquente;

import java.util.ArrayList;
import java.util.Random;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitScheduler;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.utils.BukkitEventHelper;

public class BatataQuente {

	private ArrayList<String> participantes = new ArrayList<String>();
	private boolean vip, assistirAtivado, assistirInvisivel, pvp, contarVitoria,
			contarParticipacao, ocorrendo, aberto, parte1, parte0;
	private int chamadas, tempo, id, tempoBatata, chamadascurrent,
			tempoBatataCurrent, id2;
	private double money;
	private String nome;
	private Location saida, entrada, camarote, aguarde;
	private Player vencedor, playerComBatata;
	private ArrayList<String> camarotePlayers = new ArrayList<String>();
	private YamlConfiguration config;

	public BatataQuente(final YamlConfiguration config) {
		this.config = config;
		this.tempoBatata = config.getInt("Config.Tempo_Batata_Explodir");
		this.assistirAtivado = config.getBoolean("Config.Assistir_Ativado");
		this.assistirInvisivel = config.getBoolean("Config.Assistir_Invisivel");
		this.pvp = config.getBoolean("Config.PVP");
		this.contarVitoria = config.getBoolean("Config.Contar_Vitoria");
		this.contarParticipacao = config.getBoolean("Config.Contar_Participacao");
		this.nome = config.getString("Config.Nome");
		this.chamadas = config.getInt("Config.Chamadas");
		this.tempo = config.getInt("Config.Tempo_Entre_As_Chamadas");
		this.camarote = this.getLocation("Localizacoes.Camarote");
		this.entrada = this.getLocation("Localizacoes.Entrada");
		this.aguarde = this.getLocation("Localizacoes.Aguardando");
		this.saida = this.getLocation("Localizacoes.Saida");
		this.money = config.getInt("Premios.Money") * HEventos.getHEventos().getConfig().getInt("Money_Multiplicador");
		this.aberto = false;
		this.ocorrendo = false;
		this.parte0 = false;
		this.parte1 = false;
		this.participantes.clear();
		this.chamadascurrent = this.chamadas;
		this.tempoBatata = config.getInt("Config.Tempo_Batata_Explodir");
		if (this.vip == false) {
			this.vip = config.getBoolean("Config.VIP");
		}
		this.vencedor = null;
		this.playerComBatata = null;
		this.tempoBatataCurrent = this.tempoBatata;
	}

	public void start() {
		final BukkitScheduler scheduler = HEventos.getHEventos().getServer().getScheduler();
		this.id = scheduler.scheduleSyncRepeatingTask(HEventos.getHEventos(), new Runnable() {
			@Override
			public void run() {
				if (!BatataQuente.this.parte0) {
					if (BatataQuente.this.chamadascurrent >= 1) {
						BatataQuente.this.chamadascurrent--;
						BatataQuente.this.ocorrendo = true;
						BatataQuente.this.aberto = true;
						if (BatataQuente.this.vip) {
							BatataQuente.this.sendMessageList("Mensagens.Aberto_VIP");
						} else {
							BatataQuente.this.sendMessageList("Mensagens.Aberto");
						}
					} else if (BatataQuente.this.chamadascurrent == 0) {
						if (BatataQuente.this.participantes.size() >= 1) {
							BatataQuente.this.aberto = false;
							BatataQuente.this.parte1 = true;
							BatataQuente.this.parte0 = true;
							BatataQuente.this.sendMessageList("Mensagens.Iniciando");
							for (final String sa : BatataQuente.this.camarotePlayers) {
								BatataQuente.this.getPlayerByName(sa).teleport(BatataQuente.this.camarote);
							}
							for (final String p : BatataQuente.this.participantes) {
								BatataQuente.this.getPlayerByName(p).teleport(BatataQuente.this.entrada);
								if (BatataQuente.this.contarParticipacao) {
									if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
										HEventos.getHEventos().getMysql().addPartipationPoint(p);
									} else {
										HEventos.getHEventos().getSqlite().addPartipationPoint(p);
									}
								}
							}
							final Random r = new Random();
							BatataQuente.this.playerComBatata = BatataQuente.this.getPlayerByName(BatataQuente.this.participantes.get(r.nextInt(BatataQuente.this.participantes.size())));
							BatataQuente.this.playerComBatata.getInventory().addItem(new ItemStack(Material.POTATO_ITEM, 1));
							for (final String s : BatataQuente.this.config.getStringList("Mensagens.Esta_Com_Batata")) {
								for (final String sa : BatataQuente.this.participantes) {
									final Player sab = BatataQuente.this.getPlayerByName(sa);
									sab.sendMessage(s.replace("&", "§").replace("$player$", BatataQuente.this.playerComBatata.getName()));
								}
							}
						} else {
							BatataQuente.this.reset();
							BatataQuente.this.sendMessageList("Mensagens.Cancelado");
							HEventos.getHEventos().getServer().getScheduler().cancelTask(BatataQuente.this.id);
						}
					}
				}

				if (BatataQuente.this.parte1) {
					if (BatataQuente.this.participantes.size() <= 0) {
						BatataQuente.this.sendMessageList("Mensagens.Sem_Vencedor");
						BatataQuente.this.reset();
						HEventos.getHEventos().getServer().getScheduler().cancelTask(BatataQuente.this.id);
					}
				}

			}
		}, 0, this.tempo * 20L);

		this.id2 = scheduler.scheduleSyncRepeatingTask(HEventos.getHEventos(), new Runnable() {
			@Override
			public void run() {
				if ((BatataQuente.this.ocorrendo == true) && (BatataQuente.this.aberto == false) && (BatataQuente.this.playerComBatata != null)) {
					if (BatataQuente.this.assistirInvisivel) {
						for (final String s : BatataQuente.this.participantes) {
							for (final String sa : BatataQuente.this.camarotePlayers) {
								BatataQuente.this.getPlayerByName(s).hidePlayer(BatataQuente.this.getPlayerByName(sa));
							}
						}
					}
					if (BatataQuente.this.tempoBatataCurrent > 0) {
						BatataQuente.this.tempoBatataCurrent--;
						final Location loc = BatataQuente.this.playerComBatata.getLocation();
						final Firework firework = BatataQuente.this.playerComBatata.getWorld().spawn(loc, Firework.class);
						final FireworkMeta data = firework.getFireworkMeta();
						data.addEffects(FireworkEffect.builder().withColor(Color.RED).with(Type.BALL).build());
						data.setPower(2);
						firework.setFireworkMeta(data);
						if ((BatataQuente.this.tempoBatataCurrent == 29) || (BatataQuente.this.tempoBatataCurrent == 20) || (BatataQuente.this.tempoBatataCurrent == 10) || (BatataQuente.this.tempoBatataCurrent == 5) || (BatataQuente.this.tempoBatataCurrent == 4) || (BatataQuente.this.tempoBatataCurrent == 3) || (BatataQuente.this.tempoBatataCurrent == 2) || (BatataQuente.this.tempoBatataCurrent == 1)) {
							for (final String s : BatataQuente.this.participantes) {
								final Player p = BatataQuente.this.getPlayerByName(s);
								p.playSound(p.getLocation(), Sound.CLICK, 1.0f, 1.0f);
							}
							for (final String s : BatataQuente.this.config.getStringList("Mensagens.Tempo")) {
								for (final String sa : BatataQuente.this.participantes) {
									final Player p = BatataQuente.this.getPlayerByName(sa);
									p.sendMessage(s.replace("&", "§").replace("$tempo$", BatataQuente.this.tempoBatataCurrent + ""));
								}
							}
						}
					} else {
						BatataQuente.this.playerComBatata.getInventory().removeItem(new ItemStack(Material.POTATO_ITEM, 1));
						BatataQuente.this.playerComBatata.teleport(BatataQuente.this.getSaida());
						BatataQuente.this.participantes.remove(BatataQuente.this.playerComBatata.getName());
						for (final String s : BatataQuente.this.config.getStringList("Mensagens.Eliminado")) {
							for (final String sa : BatataQuente.this.participantes) {
								final Player p = BatataQuente.this.getPlayerByName(sa);
								p.sendMessage(s.replace("&", "§").replace("$player$", BatataQuente.this.playerComBatata.getName()));
							}
						}
						final Random r = new Random();
						BatataQuente.this.playerComBatata = BatataQuente.this.getPlayerByName(BatataQuente.this.participantes.get(r.nextInt(BatataQuente.this.participantes.size())));
						for (final String s : BatataQuente.this.config.getStringList("Mensagens.Esta_Com_Batata")) {
							BatataQuente.this.playerComBatata.getInventory().addItem(new ItemStack(Material.POTATO_ITEM, 1));
							for (final String sa : BatataQuente.this.participantes) {
								final Player p = BatataQuente.this.getPlayerByName(sa);
								p.sendMessage(s.replace("&", "§").replace("$player$", BatataQuente.this.playerComBatata.getName()));
							}
						}
						BatataQuente.this.tempoBatataCurrent = BatataQuente.this.tempoBatata;
					}
					if (BatataQuente.this.participantes.size() == 1) {
						Player p = null;
						for (final String s : BatataQuente.this.participantes) {
							p = BatataQuente.this.getPlayerByName(s);
						}
						for (final String sa : BatataQuente.this.getConfig().getStringList("Premios.Itens")) {
							HEventos.getHEventos().getServer().dispatchCommand(HEventos.getHEventos().getServer().getConsoleSender(), sa.replace("$player$", p.getName()));
						}
						HEventos.getHEventos().getEconomy().depositPlayer(p.getName(), BatataQuente.this.money);
						if (BatataQuente.this.contarVitoria) {
							if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
								HEventos.getHEventos().getMysql().addWinnerPoint(p.getName());
							} else {
								HEventos.getHEventos().getSqlite().addWinnerPoint(p.getName());
							}
						}
						BatataQuente.this.encerrarEvento();
					}
				}
			}
		}, 0, 20L);
	}

	public void cancelarEvento() {
		for (final String s : this.participantes) {
			this.getPlayerByName(s).teleport(this.getSaida());
		}
		for (final String s : this.camarotePlayers) {
			this.getPlayerByName(s).teleport(this.getSaida());
		}
		this.sendMessageList("Mensagens.Cancelado");
		this.reset();
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.id);
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.id2);
	}

	public void encerrarEvento() {
		for (final String s : this.participantes) {
			this.getPlayerByName(s).teleport(this.getSaida());
		}
		for (final String s : this.camarotePlayers) {
			this.getPlayerByName(s).teleport(this.getSaida());
		}
		this.sendMessageListVencedor("Mensagens.Vencedor");
		this.reset();
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.id);
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.id2);
	}

	private void sendMessageListVencedor(final String list) {
		for (final String s : this.config.getStringList(list)) {
			HEventos.getHEventos().getServer().broadcastMessage(s.replace("&", "§").replace("$player$", this.participantes.get(0)));
		}
	}

	private void reset() {
		this.tempoBatata = this.config.getInt("Config.Tempo_Batata_Explodir");
		this.assistirAtivado = this.config.getBoolean("Config.Assistir_Ativado");
		this.assistirInvisivel = this.config.getBoolean("Config.Assistir_Invisivel");
		this.pvp = this.config.getBoolean("Config.PVP");
		this.contarVitoria = this.config.getBoolean("Config.Contar_Vitoria");
		this.contarParticipacao = this.config.getBoolean("Config.Contar_Participacao");
		this.nome = this.config.getString("Config.Nome");
		this.chamadas = this.config.getInt("Config.Chamadas");
		this.tempo = this.config.getInt("Config.Tempo_Entre_As_Chamadas");
		this.camarote = this.getLocation("Localizacoes.Camarote");
		this.entrada = this.getLocation("Localizacoes.Entrada");
		this.aguarde = this.getLocation("Localizacoes.Aguardando");
		this.saida = this.getLocation("Localizacoes.Saida");
		this.money = this.config.getInt("Premios.Money") * HEventos.getHEventos().getConfig().getInt("Money_Multiplicador");
		this.aberto = false;
		this.ocorrendo = false;
		this.parte0 = false;
		this.parte1 = false;
		this.participantes.clear();
		this.chamadascurrent = this.chamadas;
		this.tempoBatata = this.config.getInt("Config.Tempo_Batata_Explodir");
		this.vip = this.config.getBoolean("Config.VIP");
		this.vencedor = null;
		this.playerComBatata = null;
		this.tempoBatataCurrent = this.tempoBatata;
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
		HEventos.getHEventos().getEventosController().setBatataQuenteOcorrendo(null);
		BukkitEventHelper.unregisterEvents(HEventos.getHEventos().getListenersBatataQuente(), HEventos.getHEventos());
	}

	private void sendMessageList(final String list) {
		for (final String s : this.config.getStringList(list)) {
			HEventos.getHEventos().getServer().broadcastMessage(s.replace("&", "§"));
		}
	}

	private Player getPlayerByName(final String name) {
		return HEventos.getHEventos().getServer().getPlayer(name);
	}

	private Location getLocation(final String local) {
		final String world = this.config.getString(local).split(";")[0];
		final double x = Double.parseDouble(this.config.getString(local).split(";")[1]);
		final double y = Double.parseDouble(this.config.getString(local).split(";")[2]);
		final double z = Double.parseDouble(this.config.getString(local).split(";")[3]);
		return new org.bukkit.Location(HEventos.getHEventos().getServer().getWorld(world), x, y, z);
	}

	public int getTempoBatataCurrent() {
		return this.tempoBatataCurrent;
	}

	public void setTempoBatataCurrent(final int tempoBatataCurrent) {
		this.tempoBatataCurrent = tempoBatataCurrent;
	}

	public int getId2() {
		return this.id2;
	}

	public void setId2(final int id2) {
		this.id2 = id2;
	}

	public Player getPlayerComBatata() {
		return this.playerComBatata;
	}

	public void setPlayerComBatata(final Player playerComBatata) {
		this.playerComBatata = playerComBatata;
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

	public boolean isParte1() {
		return this.parte1;
	}

	public void setParte1(final boolean parte1) {
		this.parte1 = parte1;
	}

	public boolean isParte0() {
		return this.parte0;
	}

	public void setParte0(final boolean parte0) {
		this.parte0 = parte0;
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

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public int getTempoBatata() {
		return this.tempoBatata;
	}

	public void setTempoBatata(final int tempoBatata) {
		this.tempoBatata = tempoBatata;
	}

	public int getChamadascurrent() {
		return this.chamadascurrent;
	}

	public void setChamadascurrent(final int chamadascurrent) {
		this.chamadascurrent = chamadascurrent;
	}

	public double getMoney() {
		return this.money;
	}

	public void setMoney(final double money) {
		this.money = money;
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

	public Player getVencedor() {
		return this.vencedor;
	}

	public void setVencedor(final Player vencedor) {
		this.vencedor = vencedor;
	}

	public ArrayList<String> getCamarotePlayers() {
		return this.camarotePlayers;
	}

	public void setCamarotePlayers(final ArrayList<String> camarotePlayers) {
		this.camarotePlayers = camarotePlayers;
	}

	public YamlConfiguration getConfig() {
		return this.config;
	}

	public void setConfig(final YamlConfiguration config) {
		this.config = config;
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

}
