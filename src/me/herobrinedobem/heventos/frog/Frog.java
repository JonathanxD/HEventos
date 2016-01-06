package me.herobrinedobem.heventos.frog;

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

public class Frog {

	private ArrayList<String> participantes = new ArrayList<String>();
	private ArrayList<Material> blocos = new ArrayList<Material>();
	private boolean ocorrendo, aberto, parte0, podeComecar, redstoneSpawnada,
			trocarBlocos1;
	private int chamadas, tempo, id, chamadascurrent, id2, y, tempoEscolheBloco,
			tempoEscolheBlocoCurrent, tempoTrocarBlocos,
			tempoTrocarBlocosCurrent, tempoTrocarBlocos2,
			tempoTrocarBlocosCurrent2;
	private double money;
	private String nome;
	private Location saida, entrada, camarote, aguarde, redstoneBlock;
	private ArrayList<String> camarotePlayers = new ArrayList<String>();
	private Cuboid cubo;
	private YamlConfiguration config;

	public Frog(final YamlConfiguration config) {
		this.config = config;
		this.nome = config.getString("Nome");
		this.chamadas = config.getInt("Chamadas");
		this.tempo = config.getInt("Tempo_Entre_As_Chamadas");
		this.tempoEscolheBloco = config.getInt("Tempo_Escolher_Bloco");
		this.tempoTrocarBlocos = config.getInt("Tempo_Trocar_Blocos");
		this.money = config.getDouble("Premios.Money") * HEventos.getHEventos().getConfig().getInt("Money_Multiplicador");
		this.camarote = this.getLocation("Camarote");
		this.entrada = this.getLocation("Entrada");
		this.aguarde = this.getLocation("Aguardando");
		this.saida = this.getLocation("Saida");
		this.y = (int) (this.getLocation("Chao_1").getY() - 2);
		this.aberto = false;
		this.parte0 = false;
		this.ocorrendo = false;
		this.podeComecar = false;
		this.redstoneBlock = null;
		this.redstoneSpawnada = false;
		this.tempoEscolheBlocoCurrent = this.tempoEscolheBloco;
		this.tempoTrocarBlocosCurrent = this.tempoTrocarBlocos;
		this.tempoTrocarBlocosCurrent2 = this.tempoTrocarBlocos2;
		this.participantes.clear();
		this.chamadascurrent = this.chamadas;
		this.blocos.add(Material.BOOKSHELF);
		this.blocos.add(Material.DIAMOND_BLOCK);
		this.blocos.add(Material.SPONGE);
		this.blocos.add(Material.PUMPKIN);
		this.blocos.add(Material.SOUL_SAND);
		this.blocos.add(Material.NETHER_BRICK);
		this.blocos.add(Material.QUARTZ_BLOCK);
		this.blocos.add(Material.WOOD);
		this.blocos.add(Material.BEDROCK);
		this.blocos.add(Material.LOG);
		this.cubo = new Cuboid(this.getLocation("Chao_1"), this.getLocation("Chao_2"));
		for (final Block b : this.cubo.getBlocks()) {
			final Random r = new Random();
			final int i = r.nextInt(100);
			if (i <= 80) {
				b.setType(Material.SNOW_BLOCK);
			} else if ((i == 81) || (i == 82)) {
				b.setType(Material.BOOKSHELF);
			} else if ((i == 83) || (i == 84)) {
				b.setType(Material.DIAMOND_BLOCK);
			} else if ((i == 85) || (i == 86)) {
				b.setType(Material.PUMPKIN);
			} else if ((i == 87) || (i == 89)) {
				b.setType(Material.SPONGE);
			} else if ((i == 90) || (i == 91)) {
				b.setType(Material.SOUL_SAND);
			} else if ((i == 92) || (i == 93)) {
				b.setType(Material.NETHER_BRICK);
			} else if ((i == 94) || (i == 95)) {
				b.setType(Material.QUARTZ_BLOCK);
			} else if ((i == 96) || (i == 97)) {
				b.setType(Material.WOOD);
			} else if ((i == 98) || (i == 99)) {
				b.setType(Material.BEDROCK);
			} else if (i == 100) {
				b.setType(Material.LOG);
			}

			for (final Block ba : this.cubo.getBlocks()) {
				if (ba.getType() == Material.AIR) {
					final Random r1 = new Random();
					final int i1 = r1.nextInt(100);
					if (i1 <= 80) {
						b.setType(Material.SNOW_BLOCK);
					} else if ((i1 == 81) || (i1 == 82)) {
						b.setType(Material.BOOKSHELF);
					} else if ((i1 == 83) || (i1 == 84)) {
						b.setType(Material.DIAMOND_BLOCK);
					} else if ((i1 == 85) || (i1 == 86)) {
						b.setType(Material.PUMPKIN);
					} else if ((i1 == 87) || (i1 == 89)) {
						b.setType(Material.SPONGE);
					} else if ((i1 == 90) || (i1 == 91)) {
						b.setType(Material.SOUL_SAND);
					} else if ((i1 == 92) || (i1 == 93)) {
						b.setType(Material.NETHER_BRICK);
					} else if ((i1 == 94) || (i1 == 95)) {
						b.setType(Material.QUARTZ_BLOCK);
					} else if ((i1 == 96) || (i1 == 97)) {
						b.setType(Material.WOOD);
					} else if ((i1 == 98) || (i1 == 99)) {
						b.setType(Material.BEDROCK);
					} else if (i1 == 100) {
						b.setType(Material.LOG);
					}
				}
			}
		}
	}

	public void start() {
		final BukkitScheduler scheduler = HEventos.getHEventos().getServer().getScheduler();
		this.id = scheduler.scheduleSyncRepeatingTask(HEventos.getHEventos(), new Runnable() {
			@Override
			public void run() {
				if (!Frog.this.parte0) {
					if (Frog.this.chamadascurrent >= 1) {
						Frog.this.chamadascurrent--;
						Frog.this.ocorrendo = true;
						Frog.this.aberto = true;
						Frog.this.sendMessageList("Mensagens.Aberto");
					} else if (Frog.this.chamadascurrent == 0) {
						if (Frog.this.participantes.size() >= 1) {
							Frog.this.parte0 = true;
							for (final String p : Frog.this.participantes) {
								Frog.this.getPlayerByName(p).teleport(Frog.this.entrada);
								if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
									if (HEventos.getHEventos().getMysql().hasPlayer(p)) {
										HEventos.getHEventos().getMysql().updateParticipations(p, 1);
									} else {
										HEventos.getHEventos().getMysql().addNew(p, 0, 1);
									}
								} else {
									if (HEventos.getHEventos().getSqlite().hasPlayer(p)) {
										HEventos.getHEventos().getSqlite().updateParticipations(p, 1);
									} else {
										HEventos.getHEventos().getSqlite().addNew(p, 0, 1);
									}
								}
							}
							Frog.this.sendMessageList("Mensagens.Iniciando");
							for (final String s : Frog.this.config.getStringList("Mensagens.Escolher_Bloco")) {
								for (final String sa : Frog.this.participantes) {
									Frog.this.getPlayerByName(sa).sendMessage(s.replace("&", "§").replace("$tempo$", Frog.this.tempoEscolheBlocoCurrent + ""));
								}
							}
							for (final String sa : Frog.this.camarotePlayers) {
								Frog.this.getPlayerByName(sa).teleport(Frog.this.camarote);
							}
							Frog.this.aberto = false;
						} else {
							Frog.this.reset();
							Frog.this.sendMessageList("Mensagens.Cancelado");
							HEventos.getHEventos().getServer().getScheduler().cancelTask(Frog.this.id);
						}
					}
				}

				for (final String s : Frog.this.participantes) {
					for (final String sa : Frog.this.camarotePlayers) {
						Frog.this.getPlayerByName(s).hidePlayer(Frog.this.getPlayerByName(sa));
					}
				}

			}
		}, 0, this.tempo * 20L);

		this.id2 = scheduler.scheduleSyncRepeatingTask(HEventos.getHEventos(), new Runnable() {
			@Override
			public void run() {
				if ((Frog.this.ocorrendo == true) && (Frog.this.aberto == false)) {
					if (Frog.this.participantes.size() >= 1) {
						if (Frog.this.podeComecar == false) {
							if (Frog.this.tempoEscolheBlocoCurrent > 0) {
								Frog.this.tempoEscolheBlocoCurrent--;
							} else {
								for (final String s : Frog.this.config.getStringList("Mensagens.Iniciando2")) {
									for (final String sa : Frog.this.participantes) {
										Frog.this.getPlayerByName(sa).sendMessage(s.replace("&", "§"));
									}
								}
								for (final Block b : Frog.this.cubo.getBlocks()) {
									if (b.getType() == Material.SNOW_BLOCK) {
										b.setType(Material.AIR);
									}
								}
								Frog.this.podeComecar = true;
							}
						} else {
							if (Frog.this.blocos.size() > 0) {
								if (Frog.this.tempoTrocarBlocosCurrent > 0) {
									Frog.this.tempoTrocarBlocosCurrent--;
								} else {
									if (Frog.this.trocarBlocos1) {
										final Random r = new Random();
										final Material blocoRemover = Frog.this.blocos.get(r.nextInt(Frog.this.blocos.size()));
										Frog.this.blocos.remove(blocoRemover);
										for (final Block b : Frog.this.cubo.getBlocks()) {
											if (b.getType() == Material.AIR) {
												b.setType(Material.SNOW_BLOCK);
											} else if (b.getType() == blocoRemover) {
												b.setType(Material.SNOW_BLOCK);
											}
										}
										Frog.this.tempoTrocarBlocosCurrent = Frog.this.tempoTrocarBlocos;
										Frog.this.trocarBlocos1 = false;
									} else {
										for (final Block b : Frog.this.cubo.getBlocks()) {
											if (b.getType() == Material.SNOW_BLOCK) {
												b.setType(Material.AIR);
											}
										}
										Frog.this.tempoTrocarBlocosCurrent = Frog.this.tempoTrocarBlocos / 2;
										Frog.this.trocarBlocos1 = true;
									}
								}
							} else {
								if (Frog.this.redstoneSpawnada == false) {
									for (final Block b : Frog.this.cubo.getBlocks()) {
										final Random r = new Random();
										if (r.nextBoolean() == true) {
											if (b.getType() == Material.AIR) {
												b.setType(Material.SNOW_BLOCK);
											}
										}
									}
									final Random r = new Random();
									final Location spawn = Frog.this.cubo.getBlocks().get(r.nextInt(Frog.this.cubo.getBlocks().size())).getLocation();
									spawn.getBlock().setType(Material.REDSTONE_BLOCK);
									for (final String s : Frog.this.config.getStringList("Mensagens.Spawn_Final")) {
										for (final String sa : Frog.this.participantes) {
											Frog.this.getPlayerByName(sa).sendMessage(s.replace("&", "§"));
										}
									}
									Frog.this.redstoneSpawnada = true;
									Frog.this.redstoneBlock = spawn;
								}
							}
						}
					} else {
						Frog.this.encerrarEventoSemVencedor();
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

	public void encerrarEventoComVencedor(final Player p) {
		for (final String s : this.participantes) {
			final Player pa = this.getPlayerByName(s);
			pa.teleport(this.getSaida());
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
		if (HEventos.getHEventos().getConfigUtil().isMysqlAtivado()) {
			if (HEventos.getHEventos().getMysql().hasPlayer(p.getName())) {
				HEventos.getHEventos().getMysql().updateWins(p.getName(), 1);
			} else {
				HEventos.getHEventos().getMysql().addNew(p.getName(), 1, 1);
			}
		} else {
			if (HEventos.getHEventos().getSqlite().hasPlayer(p.getName())) {
				HEventos.getHEventos().getSqlite().updateWins(p.getName(), 1);
			} else {
				HEventos.getHEventos().getSqlite().addNew(p.getName(), 1, 1);
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
		this.nome = this.config.getString("Nome");
		this.chamadas = this.config.getInt("Chamadas");
		this.tempo = this.config.getInt("Tempo_Entre_As_Chamadas");
		this.tempoEscolheBloco = this.config.getInt("Tempo_Escolher_Bloco");
		this.tempoTrocarBlocos = this.config.getInt("Tempo_Trocar_Blocos");
		this.money = this.config.getDouble("Money");
		this.camarote = this.getLocation("Camarote");
		this.entrada = this.getLocation("Entrada");
		this.aguarde = this.getLocation("Aguardando");
		this.saida = this.getLocation("Saida");
		this.y = (int) (this.getLocation("Chao_1").getY() - 2);
		this.aberto = false;
		this.parte0 = false;
		this.ocorrendo = false;
		this.podeComecar = false;
		this.redstoneBlock = null;
		this.tempoEscolheBlocoCurrent = this.tempoEscolheBloco;
		this.tempoTrocarBlocosCurrent = this.tempoTrocarBlocos;
		this.tempoTrocarBlocosCurrent2 = this.tempoTrocarBlocos2;
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
		HEventos.getHEventos().getEventosController().setFrogOcorrendo(null);
		BukkitEventHelper.unregisterEvents(HEventos.getHEventos().getListenersFrog(), HEventos.getHEventos());
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

	public double getMoney() {
		return this.money;
	}

	public void setMoney(final double money) {
		this.money = money;
	}

	public int getY() {
		return this.y;
	}

	public void setY(final int y) {
		this.y = y;
	}

	public ArrayList<Material> getBlocos() {
		return this.blocos;
	}

	public void setBlocos(final ArrayList<Material> blocos) {
		this.blocos = blocos;
	}

	public boolean isPodeComecar() {
		return this.podeComecar;
	}

	public void setPodeComecar(final boolean podeComecar) {
		this.podeComecar = podeComecar;
	}

	public int getTempoEscolheBloco() {
		return this.tempoEscolheBloco;
	}

	public void setTempoEscolheBloco(final int tempoEscolheBloco) {
		this.tempoEscolheBloco = tempoEscolheBloco;
	}

	public int getTempoEscolheBlocoCurrent() {
		return this.tempoEscolheBlocoCurrent;
	}

	public void setTempoEscolheBlocoCurrent(final int tempoEscolheBlocoCurrent) {
		this.tempoEscolheBlocoCurrent = tempoEscolheBlocoCurrent;
	}

	public int getTempoTrocarBlocos() {
		return this.tempoTrocarBlocos;
	}

	public void setTempoTrocarBlocos(final int tempoTrocarBlocos) {
		this.tempoTrocarBlocos = tempoTrocarBlocos;
	}

	public int getTempoTrocarBlocosCurrent() {
		return this.tempoTrocarBlocosCurrent;
	}

	public void setTempoTrocarBlocosCurrent(final int tempoTrocarBlocosCurrent) {
		this.tempoTrocarBlocosCurrent = tempoTrocarBlocosCurrent;
	}

	public int getTempoTrocarBlocos2() {
		return this.tempoTrocarBlocos2;
	}

	public void setTempoTrocarBlocos2(final int tempoTrocarBlocos2) {
		this.tempoTrocarBlocos2 = tempoTrocarBlocos2;
	}

	public int getTempoTrocarBlocosCurrent2() {
		return this.tempoTrocarBlocosCurrent2;
	}

	public void setTempoTrocarBlocosCurrent2(final int tempoTrocarBlocosCurrent2) {
		this.tempoTrocarBlocosCurrent2 = tempoTrocarBlocosCurrent2;
	}

	public Location getRedstoneBlock() {
		return this.redstoneBlock;
	}

	public void setRedstoneBlock(final Location redstoneBlock) {
		this.redstoneBlock = redstoneBlock;
	}

	public Cuboid getCubo() {
		return this.cubo;
	}

	public void setCubo(final Cuboid cubo) {
		this.cubo = cubo;
	}

	public boolean isRedstoneSpawnada() {
		return this.redstoneSpawnada;
	}

	public void setRedstoneSpawnada(final boolean redstoneSpawnada) {
		this.redstoneSpawnada = redstoneSpawnada;
	}

}
