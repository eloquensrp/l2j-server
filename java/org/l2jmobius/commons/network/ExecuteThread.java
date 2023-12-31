package org.l2jmobius.commons.network;

import java.util.Set;
import java.util.logging.Logger;

import org.l2jmobius.commons.util.CommonUtil;

/**
 * @author Pantelis Andrianakis
 * @since September 7th 2020
 * @param <E> extends NetClient
 */
public class ExecuteThread<E extends NetClient> implements Runnable
{
	private static final Logger LOGGER = Logger.getLogger(ExecuteThread.class.getName());
	
	private final Set<E> _pool;
	private final PacketHandlerInterface<E> _packetHandler;
	private boolean _idle;
	
	public ExecuteThread(Set<E> pool, PacketHandlerInterface<E> packetHandler)
	{
		_pool = pool;
		_packetHandler = packetHandler;
	}
	
	@Override
	public void run()
	{
		while (true)
		{
			// No need to iterate when pool is empty.
			if (!_pool.isEmpty())
			{
				_idle = true;
				
				// Iterate client pool.
				ITERATE: for (E client : _pool)
				{
					if (client.getSocket() == null)
					{
						_pool.remove(client);
						continue ITERATE;
					}
					
					final byte[] data = client.getReceivedData().poll();
					if (data == null)
					{
						continue ITERATE;
					}
					
					if (client.getEncryption() != null)
					{
						try
						{
							client.getEncryption().decrypt(data, 0, data.length);
						}
						catch (Exception e)
						{
							if (client.getNetConfig().isFailedDecryptionLogged())
							{
								LOGGER.warning("ExecuteThread: Problem with " + client + " data decryption.");
								LOGGER.warning(CommonUtil.getStackTrace(e));
							}
							client.disconnect();
							continue ITERATE;
						}
					}
					_packetHandler.handle(client, new ReadablePacket(data));
					
					_idle = false;
				}
				
				// Prevent high CPU caused by repeatedly looping.
				try
				{
					Thread.sleep(_idle ? 10 : 1);
				}
				catch (Exception ignored)
				{
				}
			}
			else // Remain idle for 1 second.
			{
				// Prevent high CPU caused by repeatedly looping.
				try
				{
					Thread.sleep(1000);
				}
				catch (Exception ignored)
				{
				}
			}
		}
	}
}
