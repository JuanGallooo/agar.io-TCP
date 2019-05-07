package interfaz;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
 
@SuppressWarnings("serial")
public class Cronometro extends JPanel implements Runnable, ActionListener 
{ 
	private  PrincipalFrame principal;
	private JButton btnConectar;
	  
    private JLabel tiempo;
    private Thread hilo;
    private boolean cronometroActivo;
    
    public Cronometro(PrincipalFrame p)
    {
    	principal=p;
        setSize( 300, 200 );
        setLayout( new BorderLayout() );
 
        tiempo = new JLabel( "00:00:000" );
        tiempo.setFont( new Font( Font.SERIF, Font.BOLD, 25 ) );
        tiempo.setHorizontalAlignment( JLabel.CENTER );
        tiempo.setForeground( Color.BLACK );
        tiempo.setBackground( Color.WHITE );
        tiempo.setOpaque( true );
 
        add( tiempo, BorderLayout.CENTER );

        JLabel info= new JLabel( "Sala llena, tiempo de espara para conexión." );
        info.setFont( new Font( Font.SERIF, Font.BOLD, 25 ) );
        info.setHorizontalAlignment( JLabel.CENTER );
        info.setForeground( Color.BLUE );
        info.setBackground( Color.WHITE );
        add(info, BorderLayout.NORTH );
        
        
        JPanel aux= new JPanel();
        aux.setLayout(new GridLayout(1, 2));
        
        btnConectar = new JButton( "Conectar" );
        btnConectar.setEnabled(false);
        btnConectar.addActionListener( this );
        aux.add(btnConectar);
 
        JButton btnP = new JButton( "Streaming" );
        btnP.addActionListener( this );
        aux.add( btnP);
        
        add(aux,BorderLayout.SOUTH );
        
        setVisible( true );
        iniciarCronometro();
    }
  
    public void run(){
        Integer minutos = 0 , segundos = 59, milesimas = 999;
        String min="", seg="", mil="";
        try
        {
            while( cronometroActivo )
            {
                Thread.sleep( 4 );
                milesimas -= 4;
                 
                if( milesimas <= 0 )
                {
                    milesimas = 999;
                    segundos -= 1;
                    if( segundos <= 0 )
                    {
                        segundos = 0;
                        minutos=0;
                        milesimas=0;
                        btnConectar.setEnabled(true);
                        pararCronometro();
                    }
                }
 
                if( minutos < 10 ) min = "0" + minutos;
                else min = minutos.toString();
                if( segundos < 10 ) seg = "0" + segundos;
                else seg = segundos.toString();
                 
                if( milesimas < 10 ) mil = "00" + milesimas;
                else if( milesimas < 100 ) mil = "0" + milesimas;
                else mil = milesimas.toString();
                 
                tiempo.setText( min + ":" + seg + ":" + mil );                
            }
        }catch(Exception e){}
        tiempo.setText( "00:00:000" );
    }
  
    public void actionPerformed( ActionEvent evt ) {
        Object o = evt.getSource();
        if( o instanceof JButton )
        {
            JButton btn = (JButton)o;
            if( btn.getText().equals("Conectar") ) principal.cambiarAJuego(principal.getMundo().getJugador().getName());
            if( btn.getText().equals("Streaming") ) principal.cambiarAStreaming("");
        }
    }
  
    public void iniciarCronometro() {
        cronometroActivo = true;
        hilo = new Thread( this );
        hilo.start();
    }
  
    public void pararCronometro(){
        cronometroActivo = false;
    }

}