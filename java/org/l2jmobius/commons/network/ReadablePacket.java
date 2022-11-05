package org.l2jmobius.commons.network;

/**
 * @author Pantelis Andrianakis
 * @since October 29th 2020
 */
public class ReadablePacket
{
	private final byte[] _bytes;
	private int _position = 0;
	
	public ReadablePacket(byte[] bytes)
	{
		_bytes = bytes;
	}
	
	/**
	 * Reads <b>boolean</b> from the packet data.<br>
	 * 8bit integer (00) or (01)
	 * @return
	 */
	public boolean readBoolean()
	{
		return readByte() != 0;
	}
	
	/**
	 * Reads <b>String</b> from the packet data.
	 * @return
	 */
	public String readString()
	{
		final StringBuilder result = new StringBuilder();
		try
		{
			int charId;
			while ((charId = readShort() & 0x00ff) != 0)
			{
				result.append((char) charId);
			}
		}
		catch (Exception ignored)
		{
		}
		return result.toString();
	}
	
	/**
	 * Reads <b>byte[]</b> from the packet data.<br>
	 * 8bit integer array (00...)
	 * @param length of the array.
	 * @return
	 */
	public byte[] readBytes(int length)
	{
		final byte[] result = new byte[length];
		for (int i = 0; i < length; i++)
		{
			result[i] = _bytes[_position++];
		}
		return result;
	}
	
	/**
	 * Reads <b>byte[]</b> from the packet data.<br>
	 * 8bit integer array (00...)
	 * @param array used to store data.
	 * @return
	 */
	public byte[] readBytes(byte[] array)
	{
		for (int i = 0; i < array.length; i++)
		{
			array[i] = _bytes[_position++];
		}
		return array;
	}
	
	/**
	 * Reads <b>byte</b> from the packet data.<br>
	 * 8bit integer (00)
	 * @return
	 */
	public int readByte()
	{
		return _bytes[_position++] & 0xff;
	}
	
	/**
	 * Reads <b>short</b> from the packet data.<br>
	 * 16bit integer (00 00)
	 * @return
	 */
	public int readShort()
	{
		return (_bytes[_position++] & 0xff) //
			| ((_bytes[_position++] << 8) & 0xff00);
	}
	
	/**
	 * Reads <b>int</b> from the packet data.<br>
	 * 32bit integer (00 00 00 00)
	 * @return
	 */
	public int readInt()
	{
		return (_bytes[_position++] & 0xff) //
			| ((_bytes[_position++] << 8) & 0xff00) //
			| ((_bytes[_position++] << 16) & 0xff0000) //
			| ((_bytes[_position++] << 24) & 0xff000000);
	}
	
	/**
	 * Reads <b>long</b> from the packet data.<br>
	 * 64bit integer (00 00 00 00 00 00 00 00)
	 * @return
	 */
	public long readLong()
	{
		return (_bytes[_position++] & 0xff) //
			| ((_bytes[_position++] << 8) & 0xff00) //
			| ((_bytes[_position++] << 16) & 0xff0000) //
			| ((_bytes[_position++] << 24) & 0xff000000) //
			| (((long) _bytes[_position++] << 32) & 0xff00000000L) //
			| (((long) _bytes[_position++] << 40) & 0xff0000000000L) //
			| (((long) _bytes[_position++] << 48) & 0xff000000000000L) //
			| (((long) _bytes[_position++] << 56) & 0xff00000000000000L);
	}
	
	/**
	 * Reads <b>float</b> from the packet data.<br>
	 * 32bit single precision float (00 00 00 00)
	 * @return
	 */
	public float readFloat()
	{
		return Float.intBitsToFloat(readInt());
	}
	
	/**
	 * Reads <b>double</b> from the packet data.<br>
	 * 64bit double precision float (00 00 00 00 00 00 00 00)
	 * @return
	 */
	public double readDouble()
	{
		return Double.longBitsToDouble(readLong());
	}
	
	/**
	 * @return the number of unread bytes.
	 */
	public int getRemainingLength()
	{
		return _bytes.length - _position;
	}
	
	/**
	 * @return the byte size.
	 */
	public int getLength()
	{
		return _bytes.length;
	}
}
