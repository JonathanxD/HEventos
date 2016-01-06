package me.herobrinedobem.heventos.minamortal;

import java.util.ArrayList;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.utils.BukkitEventHelper;
import me.herobrinedobem.heventos.utils.Cuboid;

public class MinaMortal {

	private ArrayList<String> participantes = new ArrayList<String>();
	private boolean ocorrendo, aberto, parte0;
	private int chamadas, tempo, id, chamadascurrent, id2, tempoDeEvento,
			tempoDeEventoCurrent, tempoMensagens, tempoMensagensCurrent;
	private boolean vip;
	private boolean pvp;
	private boolean contarParticipacao;
	private boolean assistirAtivado;
	private boolean assistirInvisivel;
	private String nome;
	private Location saida, entrada, camarote, aguarde;
	private ArrayList<String> camarotePlayers = new ArrayList<String>();
	private YamlConfiguration config;

	public MinaMortal(final YamlConfiguration config) {
		this.config = config;
		this.nome = config.getString("Config.Nome");
		this.chamadas = config.getInt("Config.Chamadas");
		this.pvp = config.getBoolean("Config.PVP");
		if (this.vip == false) {
			this.vip = config.getBoolean("Config.VIP");
		}
		this.contarParticipacao = config.getBoolean("Config.Contar_Participacao");
		this.assistirAtivado = config.getBoolean("Config.Assistir_Ativado");
		this.assistirAtivado = config.getBoolean("Config.Assistir_Invisivel");
		this.tempo = config.getInt("Config.Tempo_Entre_As_Chamadas");
		this.camarote = this.getLocation("Localizacoes.Camarote");
		this.entrada = this.getLocation("Localizacoes.Entrada");
		this.aguarde = this.getLocation("Localizacoes.Aguardando");
		this.saida = this.getLocation("Localizacoes.Saida");
		this.tempoDeEvento = config.getInt("Config.Evento_Tempo_Minutos") * 60;
		this.tempoMensagens = config.getInt("Config.Mensagens_Tempo_Minutos") * 60;
		this.tempoDeEventoCurrent = this.tempoDeEvento;
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
				if (!MinaMortal.this.parte0) {
					if (MinaMortal.this.chamadascurrent >= 1) {
						MinaMortal.this.chamadascurrent--;
						MinaMortal.this.ocorrendo = true;
						MinaMortal.this.aberto = true;
						if (MinaMortal.this.vip) {
							MinaMortal.this.sendMessageList("Mensagens.Aberto_VIP");
						} else {
							MinaMortal.this.sendMessageList("Mensagens.Aberto");
						}
					} else if (MinaMortal.this.chamadascurrent == 0) {
						if (MinaMortal.this.participantes.size() >= 1) {
							final Cuboid cubo = new Cuboid(MinaMortal.this.getLocation("Localizacoes.Mina_1"), MinaMortal.this.getLocation("Localizacoes.Mina_2"));
							final String[] blocosConfig = MinaMortal.this.getConfig().getString("Config.Minerios").split(";");
							for (final Block b : cubo.getBlocks()) {
								final Random r = new Random();
								if (r.nextInt(100) <= MinaMortal.this.getConfig().getInt("Config.Porcentagem_De_Minerios")) {
									final String bloco = blocosConfig[r.nextInt(blocosConfig.length + 1)];
									b.setType(Material.getMaterial(Integer.parseInt(bloco)));
								} else {
									b.setType(Material.STONE);
								}
							}
							MinaMortal.this.aberto = false;
							MinaMortal.this.parte0 = true;
							MinaMortal.this.sendMessageList("Mensagens.Iniciando");
							for (final String sa : MinaMortal.this.camarotePlayers) {
								MinaMortal.this.getPlayerByName(sa).teleport(MinaMortal.this.camarote);
							}
							for (final String p : MinaMortal.this.participantes) {
								MinaMortal.this.getPlayerByName(p).teleport(MinaMortal.this.entrada);
								if (MinaMortal.this.contarParticipacao) {
									if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
										HEventos.getHEventos().getMysql().addPartipationPoint(p);
									} else {
										HEventos.getHEventos().getSqlite().addPartipationPoint(p);
									}
								}
							}
						} else {
							MinaMortal.this.reset();
							MinaMortal.this.sendMessageList("Mensagens.Cancelado");
							HEventos.getHEventos().getServer().getScheduler().cancelTask(MinaMortal.this.id);
						}
					}
				}
			}
		}, 0, this.tempo * 20L);

		this.id2 = scheduler.scheduleSyncRepeatingTask(HEventos.getHEventos(), new Runnable() {

			@Override
			public void run() {
				if ((MinaMortal.this.ocorrendo == true) && (MinaMortal.this.aberto == false)) {
					if (MinaMortal.this.participantes.size() > 0) {
						if (MinaMortal.this.tempoDeEventoCurrent > 0) {
							MinaMortal.this.tempoDeEventoCurrent--;
							if (MinaMortal.this.tempoMensagensCurrent == 0) {
								for (final String s : MinaMortal.this.config.getStringList("Mensagens.Status")) {
									HEventos.getHEventos().getServer().broadcastMessage(s.replace("&", "§").replace("$tempo$", MinaMortal.this.tempoDeEventoCurrent + ""));
								}
								MinaMortal.this.tempoMensagensCurrent = MinaMortal.this.tempoMensagens;
							} else {
								MinaMortal.this.tempoMensagensCurrent--;
							}
						} else {
							MinaMortal.this.encerrarEvento();
						}
					} else {
						MinaMortal.this.encerrarEvento();
					}
					if (MinaMortal.this.assistirInvisivel) {
						for (final String s : MinaMortal.this.participantes) {
							for (final String sa : MinaMortal.this.camarotePlayers) {
								MinaMortal.this.getPlayerByName(s).hidePlayer(MinaMortal.this.getPlayerByName(sa));
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

	public void encerrarEvento() {
		for (final String s : this.participantes) {
			this.getPlayerByName(s).teleport(this.getSaida());
		}
		for (final String s : this.camarotePlayers) {
			this.getPlayerByName(s).teleport(this.getSaida());
		}
		this.sendMessageList("Mensagens.Finalizado");
		this.reset();
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.id);
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.id2);
	}

	private void reset() {
		this.nome = this.config.getString("Config.Nome");
		this.chamadas = this.config.getInt("Config.Chamadas");
		this.pvp = this.config.getBoolean("Config.PVP");
		this.vip = this.config.getBoolean("Config.VIP");
		this.contarParticipacao = this.config.getBoolean("Config.Contar_Participacao");
		this.assistirAtivado = this.config.getBoolean("Config.Assistir_Ativado");
		this.assistirAtivado = this.config.getBoolean("Config.Assistir_Invisivel");
		this.tempo = this.config.getInt("Config.Tempo_Entre_As_Chamadas");
		this.camarote = this.getLocation("Localizacoes.Camarote");
		this.entrada = this.getLocation("Localizacoes.Entrada");
		this.aguarde = this.getLocation("Localizacoes.Aguardando");
		this.saida = this.getLocation("Localizacoes.Saida");
		this.tempoDeEvento = this.config.getInt("Config.Evento_Tempo_Minutos") * 60;
		this.tempoMensagens = this.config.getInt("Config.Mensagens_Tempo_Minutos") * 60;
		this.tempoDeEventoCurrent = this.tempoDeEvento;
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
		HEventos.getHEventos().getEventosController().setMinaMortalOcorrendo(null);
		BukkitEventHelper.unregisterEvents(HEventos.getHEventos().getListenersMinaMortal(), HEventos.getHEventos());
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

	public int getTempoDeEvento() {
		return this.tempoDeEvento;
	}

	public void setTempoDeEvento(final int tempoDeEvento) {
		this.tempoDeEvento = tempoDeEvento;
	}

	public int getTempoDeEventoCurrent() {
		return this.tempoDeEventoCurrent;
	}

	public void setTempoDeEventoCurrent(final int tempoDeEventoCurrent) {
		this.tempoDeEventoCurrent = tempoDeEventoCurrent;
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

	public boolean isVip() {
		return this.vip;
	}

	public void setVip(final boolean vip) {
		this.vip = vip;
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

}
