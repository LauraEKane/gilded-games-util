package com.gildedgames.util.modules.player.server;

import com.gildedgames.util.core.UtilModule;
import com.gildedgames.util.modules.player.PlayerModule;
import com.gildedgames.util.modules.player.common.IPlayerHookPool;
import com.gildedgames.util.modules.player.common.player.IPlayerHook;
import com.gildedgames.util.modules.player.common.player.PlayerProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class PlayerHookSaveHandler
{

	public File playerDirectory;

	public PlayerHookSaveHandler()
	{

	}

	@SubscribeEvent
	public void onSavePlayerFile(PlayerEvent.SaveToFile event)
	{
		this.playerDirectory = new File(UtilModule.getWorldDirectory(), "playerdata/");
		this.writePlayerData(UUID.fromString(event.playerUUID), PlayerModule.locate().getPools());
	}

	@SubscribeEvent
	public void onLoadPlayerFile(PlayerEvent.LoadFromFile event)
	{
		this.playerDirectory = new File(UtilModule.getWorldDirectory(), "playerdata/");
		this.readPlayerData(UUID.fromString(event.playerUUID), event.entityPlayer, PlayerModule.locate().getPools());
	}

	public void writePlayerData(UUID uuid, List<IPlayerHookPool<?>> pools)
	{
		for (IPlayerHookPool<?> manager : pools)
		{
			IPlayerHook playerHook = manager.get(uuid);

			NBTTagCompound tag = new NBTTagCompound();

			playerHook.getProfile().write(tag);

			playerHook.write(tag);

			File prefix = new File(this.playerDirectory, manager.getName());

			prefix.mkdirs();

			File tempPlayerFile = new File(prefix, uuid + ".dat.tmp");
			File playerFile = new File(prefix, uuid + ".dat");

			try
			{
				CompressedStreamTools.writeCompressed(tag, new FileOutputStream(tempPlayerFile));

				if (playerFile.exists())
				{
					playerFile.delete();
				}

				tempPlayerFile.renameTo(playerFile);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private <T extends IPlayerHook> NBTTagCompound readPlayerHook(File playerFile, IPlayerHookPool<T> pool, EntityPlayer entityplayer)
	{
		try
		{
			FileInputStream inputStream = new FileInputStream(playerFile);
			NBTTagCompound tag = CompressedStreamTools.readCompressed(inputStream);

			PlayerProfile profile = new PlayerProfile();
			profile.read(tag);
			profile.setEntity(entityplayer);

			T playerHook = pool.getFactory().create(profile, pool);

			playerHook.read(tag);

			pool.add(playerHook);

			return tag;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public void readPlayerData(UUID uuid, EntityPlayer entityplayer, List<IPlayerHookPool<?>> pools)
	{
		for (IPlayerHookPool<?> manager : pools)
		{
			if (!manager.shouldSave())
			{
				continue;
			}

			File prefix = new File(this.playerDirectory, manager.getName());

			prefix.mkdirs();

			File playerFile = new File(prefix, uuid + ".dat");

			if (playerFile.exists() && playerFile.isFile())
			{
				this.readPlayerHook(playerFile, manager, entityplayer);
			}
		}
	}

	public void flushData()
	{
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		ServerConfigurationManager configManager = server.getConfigurationManager();

		for (int i = 0; i < configManager.playerEntityList.size(); ++i)
		{
			EntityPlayerMP player = configManager.playerEntityList.get(i);

			this.writePlayerData(player.getUniqueID(), PlayerModule.locate().getPools());
		}
	}

}
