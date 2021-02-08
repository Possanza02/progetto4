import java.net.*;
import java.io.*;

package com.francescocimarra;

public class ConnessioneClient {

	private Socket connessione = null;
	private ServerPuntiDiVerifica server;
	private InputStreamReader in;
	private BufferedReader sIN;
	private OutputStream out;
	private PrintWriter sOUT;
	
	PuntoDiVerifica puntoDiVerifica;
	
	//dati per socket
	String risposta;
	String comandoRicevuto;
	
	public ConnessioneClient(Socket conn, ServerPuntiDiVerifica server)
	{
		this.connessione = conn;
		this.server = server;
		
		try
		{
			
			
			//apre i flussi in uscita e in ingresso dalla socket
			//flusso in uscita su socket
			out = connessione.getOutputStream();
			sOUT = new PrintWriter(out);
			//flusso in ingresso da socket
			in = new InputStreamReader(connessione.getInputStream());
			sIN = new BufferedReader(in);
		}
		catch (IOException e)
		{
			System.out.println(e);
		}
		
		new Thread(this).start();
		System.out.println("il client si è connesso");
	}
	
	public void run() 
	{
		try
		{
			while(true)
			{
		//riceve il comando
		comandoRicevuto = sIN.readLine();
		System.out.println(comandoRicevuto);
		if(comandoRicevuto.equals(Comando.QUIT))
			break;
		
		risposta = Protocollo.interpretaComando(comandoRicevuto, server, this);
		
		//invia il comando
		sOUT.println(risposta);
		sOUT.flush();
			}
	connessione.close();
	System.out.println("il client si è disconesso");
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}

