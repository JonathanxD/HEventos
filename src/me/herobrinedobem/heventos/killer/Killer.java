package me.herobrinedobem.heventos.killer;

import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.utils.BukkitEventHelper;

public class Killer {

	private ArrayList<String> participantes = new ArrayList<String>();
	private boolean ocorrendo, aberto, parte0;
	private boolean msgTempo = false;
	private boolean vip;
	private boolean assistirAtivado;
	private boolean assistirInvisivel;
	private boolean pvp;
	private boolean contarVitoria;
	private boolean contarParticipacao;
	private double money;
	private int chamadas, tempo, id, chamadascurrent, id2, tempoMensagens,
			tempoMensagensCurrent;
	private int tempoPegarItens;
	private int tempoPegarItensCurrent;
	private String nome;
	private Location saida, entrada, camarote, aguarde;
	private ArrayList<String> camarotePlayers = new ArrayList<String>();
	private YamlConfiguration config;

	public Killer(final YamlConfiguration config) {
		this.config = config;
		this.nome = config.getString("Config.Nome");
		this.chamadas = config.getInt("Config.Chamadas");
		this.tempoPegarItens = config.getInt("Config.Tempo_Pegar_Itens");
		this.tempoPegarItensCurrent = config.getInt("Config.Tempo_Pegar_Itens");
		this.pvp = config.getBoolean("Config.PVP");
		if (this.vip == false) {
			this.vip = config.getBoolean("Config.VIP");
		}
		this.contarParticipacao = config.getBoolean("Config.Contar_Participacao");
		this.contarVitoria = config.getBoolean("Config.Contar_Vitoria");
		this.assistirAtivado = config.getBoolean("Config.Assistir_Ativado");
		this.assistirInvisivel = config.getBoolean("Config.Assistir_Invisivel");
		this.tempo = config.getInt("Config.Tempo_Entre_As_Chamadas");
		this.camarote = this.getLocation("Localizacoes.Camarote");
		this.entrada = this.getLocation("Localizacoes.Entrada");
		this.aguarde = this.getLocation("Localizacoes.Aguardando");
		this.saida = this.getLocation("Localizacoes.Saida");
		this.tempoMensagens = config.getInt("Config.Mensagens_Tempo_Minutos") * 60;
		this.money = config.getDouble("Premios.Money") * HEventos.getHEventos().getConfig().getInt("Money_Multiplicador");
		this.tempoMensagensCurrent = this.tempoMensagens;
		this.aberto = false;
		this.parte0 = false;
		this.ocorrendo = false;
		this.participantes.clear();
		this.chamadascurrent = this.chamadas;
	}

	public void start() {
		final BukkitScheduler scheduler = HEventos.getHEventos().getServer().getScheduler();
		this.id = scheduler.scheduleSyncRepeatingTask(HEventos.getHEventos(), new Runnable() {
			@Override
			public void run() {
				if (!Killer.this.parte0) {
					if (Killer.this.chamadascurrent >= 1) {
						Killer.this.chamadascurrent--;
						Killer.this.ocorrendo = true;
						Killer.this.aberto = true;
						if (Killer.this.vip) {
							Killer.this.sendMessageList("Mensagens.Aberto_VIP");
						} else {
							Killer.this.sendMessageList("Mensagens.Aberto");
						}
					} else if (Killer.this.chamadascurrent == 0) {
						if (Killer.this.participantes.size() >= 1) {
							Killer.this.aberto = false;
							Killer.this.parte0 = true;
							Killer.this.sendMessageList("Mensagens.Iniciando");
							for (final String sa : Killer.this.camarotePlayers) {
								Killer.this.getPlayerByName(sa).teleport(Killer.this.camarote);
							}
							for (final String p : Killer.this.participantes) {
								Killer.this.getPlayerByName(p).teleport(Killer.this.entrada);
								if (Killer.this.contarParticipacao) {
									if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
										HEventos.getHEventos().getMysql().addPartipationPoint(p);
									} else {
										HEventos.getHEventos().getSqlite().addPartipationPoint(p);
									}
								}
							}
						} else {
							Killer.this.reset();
							Killer.this.sendMessageList("Mensagens.Cancelado");
							HEventos.getHEventos().getServer().getScheduler().cancelTask(Killer.this.id);
						}
					}
				}
				Killer.this.entrada.getWorld().setTime(17000);
			}
		}, 0, this.tempo * 20L);

		this.id2 = scheduler.scheduleSyncRepeatingTask(HEventos.getHEventos(), new Runnable() {
			@Override
			public void run() {
				if ((Killer.this.ocorrendo == true) && (Killer.this.aberto == false)) {
					if (Killer.this.participantes.size() > 1) {
						if (Killer.this.tempoMensagensCurrent == 0) {
							for (final String s : Killer.this.config.getStringList("Mensagens.Status")) {
								HEventos.getHEventos().getServer().broadcastMessage(s.replace("&", "§").replace("$jogadores$", Killer.this.participantes.size() + ""));
							}
							Killer.this.tempoMensagensCurrent = Killer.this.tempoMensagens;
						} else {
							Killer.this.tempoMensagensCurrent--;
						}
					} else {
						if (Killer.this.participantes.size() == 1) {
							if (Killer.this.tempoPegarItensCurrent == 0) {
								final Player p = Killer.this.getPlayerByName(Killer.this.participantes.get(0));
								Killer.this.encerrarComVencedor(p);
							} else {
								if (Killer.this.msgTempo == false) {
									for (final String s : Killer.this.participantes) {
										Killer.this.getPlayerByName(s).sendMessage(Killer.this.getConfig().getString("Mensagens.Tempo_Pegar_Itens").replace("&", "§"));
									}
									Killer.this.msgTempo = true;
								}
								Killer.this.tempoPegarItensCurrent--;
							}
						} else {
							Killer.this.encerrarSemVencedor();
						}
					}

					if (Killer.this.assistirInvisivel) {
						for (final String s : Killer.this.participantes) {
							for (final String sa : Killer.this.camarotePlayers) {
								Killer.this.getPlayerByName(s).hidePlayer(Killer.this.getPlayerByName(sa));
							}
						}
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

	public void encerrarComVencedor(final Player p) {
		for (final String s : this.participantes) {
			this.getPlayerByName(s).teleport(this.getSaida());
		}
		for (final String s : this.camarotePlayers) {
			this.getPlayerByName(s).teleport(this.getSaida());
		}
		for (final String s : this.config.getStringList("Mensagens.Vencedor")) {
			HEventos.getHEventos().getServer().broadcastMessage(s.replace("&", "§").replace("$player$", p.getName()));
		}
		for (final String s : this.config.getStringList("Premios.Comandos")) {
			HEventos.getHEventos().getServer().dispatchCommand(HEventos.getHEventos().getServer().getConsoleSender(), s.replace("$player$", p.getName()));
		}
		if (Killer.this.contarVitoria) {
			if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
				HEventos.getHEventos().getMysql().addWinnerPoint(p.getName());
			} else {
				HEventos.getHEventos().getSqlite().addWinnerPoint(p.getName());
			}
		}
		HEventos.getHEventos().getEconomy().depositPlayer(p.getName(), this.money);
		this.reset();
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.id);
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.id2);
	}

	public void encerrarSemVencedor() {
		for (final String s : this.participantes) {
			this.getPlayerByName(s).teleport(this.getSaida());
		}
		for (final String s : this.camarotePlayers) {
			this.getPlayerByName(s).teleport(this.getSaida());
		}
		this.sendMessageList("Mensagens.Sem_Vencedor");
		this.reset();
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.id);
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.id2);
	}

	private void reset() {
		this.nome = this.config.getString("Config.Nome");
		this.chamadas = this.config.getInt("Config.Chamadas");
		this.tempoPegarItens = this.config.getInt("Config.Tempo_Pegar_Itens");
		this.tempoPegarItensCurrent = this.config.getInt("Config.Tempo_Pegar_Itens");
		this.pvp = this.config.getBoolean("Config.PVP");
		this.vip = this.config.getBoolean("Config.VIP");
		this.contarParticipacao = this.config.getBoolean("Config.Contar_Participacao");
		this.contarVitoria = this.config.getBoolean("Config.Contar_Vitoria");
		this.assistirAtivado = this.config.getBoolean("Config.Assistir_Ativado");
		this.assistirInvisivel = this.config.getBoolean("Config.Assistir_Invisivel");
		this.tempo = this.config.getInt("Config.Tempo_Entre_As_Chamadas");
		this.camarote = this.getLocation("Localizacoes.Camarote");
		this.entrada = this.getLocation("Localizacoes.Entrada");
		this.aguarde = this.getLocation("Localizacoes.Aguardando");
		this.saida = this.getLocation("Localizacoes.Saida");
		this.tempoMensagens = this.config.getInt("Config.Mensagens_Tempo_Minutos") * 60;
		this.money = this.config.getDouble("Premios.Money") * HEventos.getHEventos().getConfig().getInt("Money_Multiplicador");
		this.tempoMensagensCurrent = this.tempoMensagens;
		this.aberto = false;
		this.parte0 = false;
		this.ocorrendo = false;
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
		HEventos.getHEventos().getEventosController().setKillerOcorrendo(null);
		BukkitEventHelper.unregisterEvents(HEventos.getHEventos().getListenersKiller(), HEventos.getHEventos());
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

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public int getChamadascurrent() {
		return this.chamadascurrent;
	}

	public void setChamadascurrent(final int chamadascurrent) {
		this.chamadascurrent = chamadascurrent;
	}

	public int getId2() {
		return this.id2;
	}

	public void setId2(final int id2) {
		this.id2 = id2;
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

	public boolean isContarParticipacao() {
		return this.contarParticipacao;
	}

	public void setContarParticipacao(final boolean contarParticipacao) {
		this.contarParticipacao = contarParticipacao;
	}

	public double getMoney() {
		return this.money;
	}

	public void setMoney(final double money) {
		this.money = money;
	}

	public int getTempoMensagens() {
		return this.tempoMensagens;
	}

	public void setTempoMensagens(final int tempoMensagens) {
		this.tempoMensagens = tempoMensagens;
	}

	public int getTempoMensagensCurrent() {
		return this.tempoMensagensCurrent;
	}

	public void setTempoMensagensCurrent(final int tempoMensagensCurrent) {
		this.tempoMensagensCurrent = tempoMensagensCurrent;
	}

	public int getTempoPegarItens() {
		return this.tempoPegarItens;
	}

	public void setTempoPegarItens(final int tempoPegarItens) {
		this.tempoPegarItens = tempoPegarItens;
	}

	public int getTempoPegarItensCurrent() {
		return this.tempoPegarItensCurrent;
	}

	public void setTempoPegarItensCurrent(final int tempoPegarItensCurrent) {
		this.tempoPegarItensCurrent = tempoPegarItensCurrent;
	}

}
