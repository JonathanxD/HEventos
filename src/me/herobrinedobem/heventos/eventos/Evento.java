package me.herobrinedobem.heventos.eventos;

import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.utils.BukkitEventHelper;

public class Evento {

	private ArrayList<String> participantes = new ArrayList<String>();
	private boolean ocorrendo, aberto, parte1, parte0, vip, assistirAtivado,
			assistirInvisivel, pvp, contarVitoria, contarParticipacao;
	private int chamadas, tempo, id, id2, chamadascurrent;
	private double money;
	private String nome;
	private Location saida, entrada, camarote, aguarde;
	private ArrayList<String> vencedores = new ArrayList<String>();
	private final ArrayList<String> camarotePlayers = new ArrayList<String>();
	private YamlConfiguration config;

	public Evento(final YamlConfiguration config) {
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
		this.money = config.getInt("Premios.Money") * HEventos.getHEventos().getConfig().getInt("Money_Multiplicador");
		this.aberto = false;
		this.ocorrendo = false;
		this.parte0 = false;
		this.parte1 = false;
		this.participantes.clear();
		this.chamadascurrent = this.chamadas;
	}

	public void start() {
		final BukkitScheduler scheduler = HEventos.getHEventos().getServer().getScheduler();
		this.id = scheduler.scheduleSyncRepeatingTask(HEventos.getHEventos(), new Runnable() {
			@Override
			public void run() {
				if (!Evento.this.parte0) {
					if (Evento.this.chamadascurrent >= 1) {
						Evento.this.chamadascurrent--;
						Evento.this.ocorrendo = true;
						Evento.this.aberto = true;
						if (Evento.this.vip) {
							Evento.this.sendMessageList("Mensagens.Aberto_VIP");
						} else {
							Evento.this.sendMessageList("Mensagens.Aberto");
						}
					} else if (Evento.this.chamadascurrent == 0) {
						if (Evento.this.participantes.size() >= 1) {
							Evento.this.aberto = false;
							Evento.this.parte1 = true;
							Evento.this.parte0 = true;
							Evento.this.sendMessageList("Mensagens.Iniciando");
							for (final String sa : Evento.this.camarotePlayers) {
								Evento.this.getPlayerByName(sa).teleport(Evento.this.camarote);
							}
							for (final String p : Evento.this.participantes) {
								Evento.this.getPlayerByName(p).teleport(Evento.this.entrada);
								if (Evento.this.contarParticipacao) {
									if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
										HEventos.getHEventos().getMysql().addPartipationPoint(p);
									} else {
										HEventos.getHEventos().getSqlite().addPartipationPoint(p);
									}
								}
							}
						} else {
							Evento.this.reset();
							Evento.this.sendMessageList("Mensagens.Cancelado");
							HEventos.getHEventos().getServer().getScheduler().cancelTask(Evento.this.id);
						}
					}
				}

				if (Evento.this.parte1) {
					if (Evento.this.participantes.size() <= 0) {
						Evento.this.sendMessageList("Mensagens.Sem_Vencedor");
						Evento.this.reset();
						HEventos.getHEventos().getServer().getScheduler().cancelTask(Evento.this.id);
					} else if ((Evento.this.participantes.size() == 1) && (Evento.this.vencedores.size() == 1)) {
						/*
						 * for (final String sa : Evento.this.getConfig().getStringList("Premios.Primeiro_Lugar")) {
						 * HEventos.getHEventos().getServer().dispatchCommand(HEventos.getHEventos().getServer().getConsoleSender(), sa.replace("$player$",
						 * Evento.this.getVencedores().get(0)));
						 * }
						 * HEventos.getHEventos().getEconomy().depositPlayer(Evento.this.getVencedores().get(0), Evento.this.money);
						 */
						if (Evento.this.contarVitoria) {
							if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
								HEventos.getHEventos().getMysql().addWinnerPoint(Evento.this.getVencedores().get(0));
							} else {
								HEventos.getHEventos().getSqlite().addWinnerPoint(Evento.this.getVencedores().get(0));
							}
						}
						Evento.this.encerrarEvento();
					} else if ((Evento.this.participantes.size() == 2) && (Evento.this.vencedores.size() == 2)) {
						/*
						 * for (final String sa : Evento.this.getConfig().getStringList("Premios.Primeiro_Lugar")) {
						 * HEventos.getHEventos().getServer().dispatchCommand(HEventos.getHEventos().getServer().getConsoleSender(), sa.replace("$player$",
						 * Evento.this.getVencedores().get(0)));
						 * }
						 * HEventos.getHEventos().getEconomy().depositPlayer(Evento.this.getVencedores().get(0), Evento.this.money);
						 */
						if (Evento.this.contarVitoria) {
							if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
								HEventos.getHEventos().getMysql().addWinnerPoint(Evento.this.getVencedores().get(0));
							} else {
								HEventos.getHEventos().getSqlite().addWinnerPoint(Evento.this.getVencedores().get(0));
							}
						}
						/*
						 * for (final String sa : Evento.this.getConfig().getStringList("Premios.Segundo_Lugar")) {
						 * HEventos.getHEventos().getServer().dispatchCommand(HEventos.getHEventos().getServer().getConsoleSender(), sa.replace("$player$",
						 * Evento.this.getVencedores().get(1)));
						 * }
						 * HEventos.getHEventos().getEconomy().depositPlayer(Evento.this.getVencedores().get(1), Evento.this.money);
						 */
						if (Evento.this.contarVitoria) {
							if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
								HEventos.getHEventos().getMysql().addWinnerPoint(Evento.this.getVencedores().get(1));
							} else {
								HEventos.getHEventos().getSqlite().addWinnerPoint(Evento.this.getVencedores().get(1));
							}
						}
						Evento.this.encerrarEvento();
					} else if ((Evento.this.participantes.size() == 2) && (Evento.this.vencedores.size() == 2)) {
						/*
						 * for (final String sa : Evento.this.getConfig().getStringList("Premios.Primeiro_Lugar")) {
						 * HEventos.getHEventos().getServer().dispatchCommand(HEventos.getHEventos().getServer().getConsoleSender(), sa.replace("$player$",
						 * Evento.this.getVencedores().get(0)));
						 * }
						 * HEventos.getHEventos().getEconomy().depositPlayer(Evento.this.getVencedores().get(0), Evento.this.money);
						 */
						if (Evento.this.contarVitoria) {
							if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
								HEventos.getHEventos().getMysql().addWinnerPoint(Evento.this.getVencedores().get(0));
							} else {
								HEventos.getHEventos().getSqlite().addWinnerPoint(Evento.this.getVencedores().get(0));
							}
						}
						/*
						 * for (final String sa : Evento.this.getConfig().getStringList("Premios.Segundo_Lugar")) {
						 * HEventos.getHEventos().getServer().dispatchCommand(HEventos.getHEventos().getServer().getConsoleSender(), sa.replace("$player$",
						 * Evento.this.getVencedores().get(1)));
						 * }
						 * HEventos.getHEventos().getEconomy().depositPlayer(Evento.this.getVencedores().get(1), Evento.this.money);
						 */
						if (Evento.this.contarVitoria) {
							if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
								HEventos.getHEventos().getMysql().addWinnerPoint(Evento.this.getVencedores().get(1));
							} else {
								HEventos.getHEventos().getSqlite().addWinnerPoint(Evento.this.getVencedores().get(1));
							}
						}
						/*
						 * for (final String sa : Evento.this.getConfig().getStringList("Premios.Terceiro_Lugar")) {
						 * HEventos.getHEventos().getServer().dispatchCommand(HEventos.getHEventos().getServer().getConsoleSender(), sa.replace("$player$",
						 * Evento.this.getVencedores().get(2)));
						 * }
						 * HEventos.getHEventos().getEconomy().depositPlayer(Evento.this.getVencedores().get(2), Evento.this.money);
						 */
						if (Evento.this.contarVitoria) {
							if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
								HEventos.getHEventos().getMysql().addWinnerPoint(Evento.this.getVencedores().get(2));
							} else {
								HEventos.getHEventos().getSqlite().addWinnerPoint(Evento.this.getVencedores().get(2));
							}
						}
						Evento.this.encerrarEvento();
					}
				}

			}
		}, 0, this.tempo * 20L);

		if (this.assistirInvisivel) {
			this.id2 = scheduler.scheduleSyncRepeatingTask(HEventos.getHEventos(), new Runnable() {
				@Override
				public void run() {
					for (final String s : Evento.this.participantes) {
						for (final String sa : Evento.this.camarotePlayers) {
							Evento.this.getPlayerByName(s).hidePlayer(Evento.this.getPlayerByName(sa));
						}
					}
				}
			}, 0, 20L);
		}
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
	}

	private void reset() {
		this.nome = this.config.getString("Config.Nome");
		this.chamadas = this.config.getInt("Config.Chamadas");
		this.vip = this.config.getBoolean("Config.VIP");
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
		this.money = this.config.getInt("Premios.Money") * HEventos.getHEventos().getConfig().getInt("Money_Multiplicador");
		this.aberto = false;
		this.ocorrendo = false;
		this.parte0 = false;
		this.parte1 = false;
		this.participantes.clear();
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
		for (final Player p : HEventos.getHEventos().getServer().getOnlinePlayers()) {
			p.setScoreboard(HEventos.getHEventos().getServer().getScoreboardManager().getMainScoreboard());
		}
		HEventos.getHEventos().getEventosController().setEventoOcorrendo(null);
		BukkitEventHelper.unregisterEvents(HEventos.getHEventos().getListeners(), HEventos.getHEventos());
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.id);
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.id2);
	}

	public void removePlayerWinnerForEvent(final Player p, final int pos) {
		p.teleport(this.getSaida());
		this.participantes.remove(p.getName());
		if (pos == 1) {
			this.getVencedores().add(0, p.getName());
			for (final String sa : this.getConfig().getStringList("Mensagens.Lugar")) {
				HEventos.getHEventos().getServer().broadcastMessage(sa.replace("&", "§").replace("$player$", p.getName()).replace("$posicao$", "1°"));
			}
			for (final String sa : this.getConfig().getStringList("Premios.Primeiro_Lugar")) {
				HEventos.getHEventos().getServer().dispatchCommand(HEventos.getHEventos().getServer().getConsoleSender(), sa.replace("$player$", this.getVencedores().get(0)));
			}

			final double money = this.money * HEventos.getHEventos().getConfig().getInt("Money_Multiplicador");
			HEventos.getHEventos().getServer().dispatchCommand(HEventos.getHEventos().getServer().getConsoleSender(), "money give " + this.getVencedores().get(0) + " " + money);
			if (this.contarVitoria) {
				if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
					HEventos.getHEventos().getMysql().addWinnerPoint(Evento.this.getVencedores().get(0));
				} else {
					HEventos.getHEventos().getSqlite().addWinnerPoint(Evento.this.getVencedores().get(0));
				}
			}
		} else if (pos == 2) {
			this.getVencedores().add(1, p.getName());
			for (final String sa : this.getConfig().getStringList("Mensagens.Lugar")) {
				HEventos.getHEventos().getServer().broadcastMessage(sa.replace("&", "§").replace("$player$", p.getName()).replace("$posicao$", "2°"));
			}
			for (final String sa : this.getConfig().getStringList("Premios.Segundo_Lugar")) {
				HEventos.getHEventos().getServer().dispatchCommand(HEventos.getHEventos().getServer().getConsoleSender(), sa.replace("$player$", this.getVencedores().get(1)));
			}

			final double money = this.money * HEventos.getHEventos().getConfig().getInt("Money_Multiplicador");
			HEventos.getHEventos().getServer().dispatchCommand(HEventos.getHEventos().getServer().getConsoleSender(), "money give " + this.getVencedores().get(1) + " " + money);
			if (this.contarVitoria) {
				if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
					HEventos.getHEventos().getMysql().addWinnerPoint(Evento.this.getVencedores().get(1));
				} else {
					HEventos.getHEventos().getSqlite().addWinnerPoint(Evento.this.getVencedores().get(1));
				}
			}
		} else if (pos == 3) {
			this.getVencedores().add(2, p.getName());
			for (final String sa : this.getConfig().getStringList("Mensagens.Lugar")) {
				HEventos.getHEventos().getServer().broadcastMessage(sa.replace("&", "§").replace("$player$", p.getName()).replace("$posicao$", "3°"));
			}
			for (final String sa : this.getConfig().getStringList("Premios.Terceiro_Lugar")) {
				HEventos.getHEventos().getServer().dispatchCommand(HEventos.getHEventos().getServer().getConsoleSender(), sa.replace("$player$", this.getVencedores().get(2)));
			}

			final double money = this.money * HEventos.getHEventos().getConfig().getInt("Money_Multiplicador");
			HEventos.getHEventos().getServer().dispatchCommand(HEventos.getHEventos().getServer().getConsoleSender(), "money give " + this.getVencedores().get(2) + " " + money);
			if (this.contarVitoria) {
				if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
					HEventos.getHEventos().getMysql().addWinnerPoint(Evento.this.getVencedores().get(2));
				} else {
					HEventos.getHEventos().getSqlite().addWinnerPoint(Evento.this.getVencedores().get(2));
				}
			}
			this.encerrarEvento();
		}
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

	// NATHAMPA

}
