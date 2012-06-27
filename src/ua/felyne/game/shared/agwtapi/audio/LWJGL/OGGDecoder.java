package ua.felyne.game.shared.agwtapi.audio.LWJGL;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class OGGDecoder {
	
	public OGGStruct getData(InputStream input) throws IOException
	{
		OGGStruct ogg = new OGGStruct();
		ByteArrayOutputStream dataout = new ByteArrayOutputStream();
		OGGStream inputOgg = new OGGStream(input);
		
		//Lectura de fichero ogg a data
		while(!inputOgg.atEnd()) {
			dataout.write(inputOgg.read());
		}
		
		//Añadimos los datos, los canales y la frecuencia de muestreo
		byte[] data = dataout.toByteArray();
		ogg.setData(ByteBuffer.allocateDirect(data.length));
		ogg.getData().put(data);
		ogg.getData().rewind();
		ogg.setChannel(inputOgg.getChannels());
		ogg.setRate(inputOgg.getRate());
		 
		return ogg;
	}
}
