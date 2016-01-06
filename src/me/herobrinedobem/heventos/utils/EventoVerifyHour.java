package me.herobrinedobem.heventos.utils;

import java.util.Calendar;
import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.batataquente.BatataQuente;
import me.herobrinedobem.heventos.eventos.Evento;
import me.herobrinedobem.heventos.frog.Frog;
import me.herobrinedobem.heventos.paintball.Paintball;
import me.herobrinedobem.heventos.spleef.Spleef;

public class EventoVerifyHour extends Thread {

	@Override
	public void run() {
		while (true) {
			final Calendar cal = Calendar.getInstance();
			final String hora = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
			final String minutos = String.valueOf(cal.get(Calendar.MINUTE));
			for (final String s : HEventos.getHEventos().getConfig().getStringList("Horarios")) {
				if (s.startsWith(hora + ":" + minutos)) {
					if ((HEventos.getHEventos().getEventosController().getEventoOcorrendo() == null) && (HEventos.getHEventos().getEventosController().getPaintballOcorrendo() == null) && (HEventos.getHEventos().getEventosController().getBatataQuenteOcorrendo() == null) && (HEventos.getHEventos().getEventosController().getMinaMortalOcorrendo() == null) && (HEventos.getHEventos().getEventosController().getSpleefOcorrendo() == null) && (HEventos.getHEventos().getEventosController().getKillerOcorrendo() == null) && (HEventos.getHEventos().getEventosController().getFrogOcorrendo() == null)) {
						if (s.split("-")[1].contains("batataquente")) {
							HEventos.getHEventos().getServer().getPluginManager().registerEvents(HEventos.getHEventos().getListenersBatataQuente(), HEventos.getHEventos());
							final BatataQuente bq = HEventos.getHEventos().getEventosController().loadEventoBatataQuente(s.split("-")[1]);
							HEventos.getHEventos().getEventosController().setBatataQuenteOcorrendo(bq);
							bq.start();
						} else if (s.split("-")[1].contains("frog")) {
							HEventos.getHEventos().getServer().getPluginManager().registerEvents(HEventos.getHEventos().getListenersFrog(), HEventos.getHEventos());
							final Frog bq = HEventos.getHEventos().getEventosController().loadEventoFrog(s.split("-")[1]);
							HEventos.getHEventos().getEventosController().setFrogOcorrendo(bq);
							bq.start();
						} else if (s.split("-")[1].contains("spleef")) {
							HEventos.getHEventos().getServer().getPluginManager().registerEvents(HEventos.getHEventos().getListenersSpleef(), HEventos.getHEventos());
							final Spleef bq = HEventos.getHEventos().getEventosController().loadEventoSpleef(s.split("-")[1]);
							HEventos.getHEventos().getEventosController().setSpleefOcorrendo(bq);
							bq.start();
						} else if (s.split("-")[1].contains("paintball")) {
							HEventos.getHEventos().getServer().getPluginManager().registerEvents(HEventos.getHEventos().getListenersPaintball(), HEventos.getHEventos());
							final Paintball bq = HEventos.getHEventos().getEventosController().loadEventoPaintBall(s.split("-")[1]);
							HEventos.getHEventos().getEventosController().setPaintBallOcorrendo(bq);
							bq.start();
						} else {
							final Evento e = HEventos.getHEventos().getEventosController().loadEvento(s.split("-")[1]);
							HEventos.getHEventos().getEventosController().setEventoOcorrendo(e);
							e.start();
						}
					}
				}
			}
			try {
				Thread.sleep(10 * 1000);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
