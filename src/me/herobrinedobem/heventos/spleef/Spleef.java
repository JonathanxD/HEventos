package me.herobrinedobem.heventos.spleef;

import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.utils.BukkitEventHelper;
import me.herobrinedobem.heventos.utils.Cuboid;

public class Spleef {

	private ArrayList<String> participantes = new ArrayList<String>();
	private boolean ocorrendo, aberto, parte0, podeQuebrar, vencedorEscolhido;
	private boolean vip;
	private boolean assistirAtivado;
	private boolean assistirInvisivel;
	private boolean pvp;
	private boolean contarVitoria;
	private boolean contarParticipacao;
	private boolean regenerarChao;
	private int chamadas, tempo, id, chamadascurrent, id2, tempoChaoRegenera,
			tempoChaoRegeneraCurrent, y, tempoComecar, tempoComecarCurrent;
	private double money;
	private String nome;
	private Location saida, entrada, camarote, aguarde;
	private ArrayList<String> camarotePlayers = new ArrayList<String>();
	private YamlConfiguration config;

	public Spleef(final YamlConfiguration config) {
		this.config = config;
		this.nome = config.getString("Config.Nome");
		this.chamadas = config.getInt("Config.Chamadas");
		this.pvp = config.getBoolean("Config.PVP");
		if (this.vip == false) {
			this.vip = config.getBoolean("Config.VIP");
		}
		this.regenerarChao = config.getBoolean("Config.Regenerar_Chao");
		this.assistirAtivado = config.getBoolean("Config.Assistir_Ativado");
		this.assistirInvisivel = config.getBoolean("Config.Assistir_Invisivel");
		this.contarVitoria = config.getBoolean("Config.Contar_Vitoria");
		this.contarParticipacao = config.getBoolean("Config.Contar_Participacao");
		this.tempo = config.getInt("Config.Tempo_Entre_As_Chamadas");
		this.tempoChaoRegenera = config.getInt("Config.Tempo_Chao_Regenera");
		this.tempoComecar = config.getInt("Config.Tempo_Comecar");
		this.money = config.getDouble("Premios.Money") * HEventos.getHEventos().getConfig().getInt("Money_Multiplicador");
		this.camarote = this.getLocation("Localizacoes.Camarote");
		this.entrada = this.getLocation("Localizacoes.Entrada");
		this.aguarde = this.getLocation("Localizacoes.Aguardando");
		this.saida = this.getLocation("Localizacoes.Saida");
		this.y = (int) (this.getLocation("Localizacoes.Chao_1").getY() - 2);
		this.aberto = false;
		this.parte0 = false;
		this.podeQuebrar = false;
		this.ocorrendo = false;
		this.participantes.clear();
		this.tempoChaoRegeneraCurrent = this.tempoChaoRegenera;
		this.tempoComecarCurrent = this.tempoComecar;
		this.chamadascurrent = this.chamadas;
	}

	public void start() {
		final BukkitScheduler scheduler = HEventos.getHEventos().getServer().getScheduler();
		this.id = scheduler.scheduleSyncRepeatingTask(HEventos.getHEventos(), new Runnable() {
			@Override
			public void run() {
				if (!Spleef.this.parte0) {
					if (Spleef.this.chamadascurrent >= 1) {
						Spleef.this.chamadascurrent--;
						Spleef.this.ocorrendo = true;
						Spleef.this.aberto = true;
						if (Spleef.this.vip) {
							Spleef.this.sendMessageList("Mensagens.Aberto_VIP");
						} else {
							Spleef.this.sendMessageList("Mensagens.Aberto");
						}
					} else if (Spleef.this.chamadascurrent == 0) {
						if (Spleef.this.participantes.size() >= 1) {
							Spleef.this.parte0 = true;
							final Cuboid cubo = new Cuboid(Spleef.this.getLocation("Localizacoes.Chao_1"), Spleef.this.getLocation("Localizacoes.Chao_2"));
							for (final Block b : cubo.getBlocks()) {
								b.setType(Material.getMaterial(Spleef.this.config.getInt("Config.Chao_ID")));
							}
							for (final String p : Spleef.this.participantes) {
								Spleef.this.getPlayerByName(p).teleport(Spleef.this.entrada);
								if (Spleef.this.contarParticipacao) {
									if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
										HEventos.getHEventos().getMysql().addPartipationPoint(p);
									} else {
										HEventos.getHEventos().getSqlite().addPartipationPoint(p);
									}
								}
								for (final int i : Spleef.this.config.getIntegerList("Itens_Ao_Iniciar")) {
									Spleef.this.getPlayerByName(p).getInventory().addItem(new ItemStack(Material.getMaterial(i), 1));
								}
							}
							Spleef.this.sendMessageList("Mensagens.Iniciando");
							for (final String sa : Spleef.this.camarotePlayers) {
								Spleef.this.getPlayerByName(sa).teleport(Spleef.this.camarote);
							}
							Spleef.this.aberto = false;
						} else {
							Spleef.this.reset();
							Spleef.this.sendMessageList("Mensagens.Cancelado");
							HEventos.getHEventos().getServer().getScheduler().cancelTask(Spleef.this.id);
						}
					}
				}
			}
		}, 0, this.tempo * 20L);

		this.id2 = scheduler.scheduleSyncRepeatingTask(HEventos.getHEventos(), new Runnable() {
			@Override
			public void run() {
				if ((Spleef.this.ocorrendo == true) && (Spleef.this.aberto == false)) {
					Spleef.this.tempoComecarCurrent--;

					if (Spleef.this.tempoComecarCurrent == 0) {
						Spleef.this.podeQuebrar = true;
					}

					if (Spleef.this.regenerarChao) {
						Spleef.this.tempoChaoRegeneraCurrent--;
						if (Spleef.this.tempoChaoRegeneraCurrent == 0) {
							final Cuboid cubo = new Cuboid(Spleef.this.getLocation("Localizacoes.Chao_1"), Spleef.this.getLocation("Localizacoes.Chao_2"));
							for (final Block b : cubo.getBlocks()) {
								b.setType(Material.getMaterial("Config.Chao_ID"));
							}
							Spleef.this.tempoChaoRegeneraCurrent = Spleef.this.tempoChaoRegenera;
						}
					}

					if (Spleef.this.participantes.size() == 1) {
						Spleef.this.encerrarEventoComVencedor();
					}

					if (Spleef.this.vencedorEscolhido == false) {
						if (Spleef.this.participantes.size() == 0) {
							Spleef.this.encerrarEventoSemVencedor();
						}
					}

					if (Spleef.this.assistirInvisivel) {
						for (final String s : Spleef.this.participantes) {
							for (final String sa : Spleef.this.camarotePlayers) {
								Spleef.this.getPlayerByName(s).hidePlayer(Spleef.this.getPlayerByName(sa));
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

	public void encerrarEventoComVencedor() {
		this.vencedorEscolhido = true;
		Player p = null;
		for (final String s : this.participantes) {
			p = this.getPlayerByName(s);
			p.teleport(this.getSaida());
		}
		for (final String s : this.camarotePlayers) {
			this.getPlayerByName(s).teleport(this.getSaida());
		}
		for (final String s : this.config.getStringList("Mensagens.Vencedor")) {
			HEventos.getHEventos().getServer().broadcastMessage(s.replace("&", "§").replace("$player$", p.getName()));
		}
		for (final String sa : this.getConfig().getStringList("Premios.Itens")) {
			HEventos.getHEventos().getServer().dispatchCommand(HEventos.getHEventos().getServer().getConsoleSender(), sa.replace("$player$", p.getName()));
		}
		HEventos.getHEventos().getEconomy().depositPlayer(p.getName(), this.money);
		if (Spleef.this.contarVitoria) {
			if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
				HEventos.getHEventos().getMysql().addWinnerPoint(p.getName());
			} else {
				HEventos.getHEventos().getSqlite().addWinnerPoint(p.getName());
			}
		}
		this.reset();
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.id);
		HEventos.getHEventos().getServer().getScheduler().cancelTask(this.id2);
	}

	public void encerrarEventoSemVencedor() {
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
		this.pvp = this.config.getBoolean("Config.PVP");
		this.vip = this.config.getBoolean("Config.VIP");
		this.assistirAtivado = this.config.getBoolean("Config.Assistir_Ativado");
		this.assistirInvisivel = this.config.getBoolean("Config.Assistir_Invisivel");
		this.contarVitoria = this.config.getBoolean("Config.Contar_Vitoria");
		this.contarParticipacao = this.config.getBoolean("Config.Contar_Participacao");
		this.tempo = this.config.getInt("Config.Tempo_Entre_As_Chamadas");
		this.tempoChaoRegenera = this.config.getInt("Config.Tempo_Chao_Regenera");
		this.tempoComecar = this.config.getInt("Config.Tempo_Comecar");
		this.money = this.config.getDouble("Premios.Money") * HEventos.getHEventos().getConfig().getInt("Money_Multiplicador");
		this.camarote = this.getLocation("Localizacoes.Camarote");
		this.entrada = this.getLocation("Localizacoes.Entrada");
		this.aguarde = this.getLocation("Localizacoes.Aguardando");
		this.saida = this.getLocation("Localizacoes.Saida");
		this.y = (int) (this.getLocation("Localizacoes.Chao_1").getY() - 2);
		this.aberto = false;
		this.parte0 = false;
		this.podeQuebrar = false;
		this.ocorrendo = false;
		this.participantes.clear();
		this.tempoChaoRegeneraCurrent = this.tempoChaoRegenera;
		this.tempoComecarCurrent = this.tempoComecar;
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
		HEventos.getHEventos().getEventosController().setSpleefOcorrendo(null);
		BukkitEventHelper.unregisterEvents(HEventos.getHEventos().getListenersSpleef(), HEventos.getHEventos());
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

	public int getTempoChaoRegenera() {
		return this.tempoChaoRegenera;
	}

	public void setTempoChaoRegenera(final int tempoChaoRegenera) {
		this.tempoChaoRegenera = tempoChaoRegenera;
	}

	public int getTempoChaoRegeneraCurrent() {
		return this.tempoChaoRegeneraCurrent;
	}

	public void setTempoChaoRegeneraCurrent(final int tempoChaoRegeneraCurrent) {
		this.tempoChaoRegeneraCurrent = tempoChaoRegeneraCurrent;
	}

	public double getMoney() {
		return this.money;
	}

	public void setMoney(final double money) {
		this.money = money;
	}

	public int getY() {
		return this.y;
	}

	public boolean isPodeQuebrar() {
		return this.podeQuebrar;
	}

	public void setPodeQuebrar(final boolean podeQuebrar) {
		this.podeQuebrar = podeQuebrar;
	}

	public int getTempoComecar() {
		return this.tempoComecar;
	}

	public void setTempoComecar(final int tempoComecar) {
		this.tempoComecar = tempoComecar;
	}

	public int getTempoComecarCurrent() {
		return this.tempoComecarCurrent;
	}

	public void setTempoComecarCurrent(final int tempoComecarCurrent) {
		this.tempoComecarCurrent = tempoComecarCurrent;
	}

	public void setY(final int y) {
		this.y = y;
	}

	public boolean isVencedorEscolhido() {
		return this.vencedorEscolhido;
	}

	public void setVencedorEscolhido(final boolean vencedorEscolhido) {
		this.vencedorEscolhido = vencedorEscolhido;
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

	public boolean isRegenerarChao() {
		return this.regenerarChao;
	}

	public void setRegenerarChao(final boolean regenerarChao) {
		this.regenerarChao = regenerarChao;
	}

}
