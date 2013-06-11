/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2013
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import Reika.DragonAPI.RegistrationException;
import Reika.DragonAPI.Libraries.ReikaJavaLibrary;
import Reika.DragonAPI.Libraries.ReikaMathLibrary;
import Reika.RotaryCraft.Base.ItemBasic;
import Reika.RotaryCraft.Base.ItemChargedTool;
import Reika.RotaryCraft.Base.ItemRotaryTool;
import Reika.RotaryCraft.Items.ItemBedrockAxe;
import Reika.RotaryCraft.Items.ItemBedrockPickaxe;
import Reika.RotaryCraft.Items.ItemBedrockShovel;
import Reika.RotaryCraft.Items.ItemCanolaSeed;
import Reika.RotaryCraft.Items.ItemCoil;
import Reika.RotaryCraft.Items.ItemDebug;
import Reika.RotaryCraft.Items.ItemFireballLauncher;
import Reika.RotaryCraft.Items.ItemFuelLubeBucket;
import Reika.RotaryCraft.Items.ItemGravelGun;
import Reika.RotaryCraft.Items.ItemHandBook;
import Reika.RotaryCraft.Items.ItemHandheldCrafting;
import Reika.RotaryCraft.Items.ItemIOGoggles;
import Reika.RotaryCraft.Items.ItemMeter;
import Reika.RotaryCraft.Items.ItemMotionTracker;
import Reika.RotaryCraft.Items.ItemNightVisionGoggles;
import Reika.RotaryCraft.Items.ItemNightVisionHelmet;
import Reika.RotaryCraft.Items.ItemRailGunAmmo;
import Reika.RotaryCraft.Items.ItemScrewdriver;
import Reika.RotaryCraft.Items.ItemStunGun;
import Reika.RotaryCraft.Items.ItemTarget;
import Reika.RotaryCraft.Items.ItemUltrasound;
import Reika.RotaryCraft.Items.ItemVacuum;
import Reika.RotaryCraft.Items.ItemWorldEdit;

public enum ItemRegistry {

	SCREWDRIVER(0, false, 		"Screwdriver", 				ItemScrewdriver.class),
	METER(16, false, 			"Angular Transducer", 		ItemMeter.class),
	DEBUG(112, false, 			"Magic Wand", 				ItemDebug.class),
	WORLDEDIT(114, 115, true, 	"WorldEdit Tool", 			ItemWorldEdit.class),
	HANDBOOK(208, false, 		"RotaryCraft Handbook", 	ItemHandBook.class),
	YEAST(32, false, 			"Yeast", 					ItemBasic.class),
	ETHANOL(64, false, 			"Ethanol Crystals", 		ItemBasic.class),
	CANOLA(80, true, 			"Canola Seeds", 			ItemCanolaSeed.class),
	SPRING(96, true, 			"#Wind Spring", 			ItemCoil.class),
	ULTRASOUND(128, true, 		"Ultrasound", 				ItemUltrasound.class),
	MOTION(144, true, 			"Motion Tracker", 			ItemMotionTracker.class),
	VACUUM(160, true, 			"Vacuum", 					ItemVacuum.class),
	STUNGUN(192, true, 			"Knockback Gun", 			ItemStunGun.class),
	GRAVELGUN(176, true, 		"Gravel Gun", 				ItemGravelGun.class),
	FIREBALL(224, 232, true, 	"Fireball Launcher", 		ItemFireballLauncher.class),
	BEDPICK(101, false, 		"Bedrock Pickaxe", 			ItemBedrockPickaxe.class),
	BEDAXE(100, false, 			"Bedrock Axe", 				ItemBedrockAxe.class),
	BEDSHOVEL(102, false, 		"Bedrock Shovel", 			ItemBedrockShovel.class),
	NVG(97, true, 				"Night Vision Goggles", 	ItemNightVisionGoggles.class),
	NVH(48, true, 				"Night Vision Helmet", 		ItemNightVisionHelmet.class),
	HANDCRAFT(33, false, 		"Handheld Crafting Tool", 	ItemHandheldCrafting.class),
	RAILGUN(113, true, 			"#Railgun Ammunition", 		ItemRailGunAmmo.class),
	BUCKET(104, 106, true, 		"#Bucket", 					ItemFuelLubeBucket.class),
	TARGET(98, false, 			"TNT Cannon Targeting Aid", ItemTarget.class),
	IOGOGGLES(1, true, 			"I/O Goggles", 				ItemIOGoggles.class),
	SLIDE(2, true, 				"#Projector Slides", 		ItemBasic.class);

	private int index;
	private boolean hasSubtypes;
	private String name;
	private Class itemClass;
	private int texturesheet;

	private int maxindex;

	private ItemRegistry(int tex, boolean sub, String n, Class <?extends Item> iCl) {
		texturesheet = 1;
		if (tex < 0) {
			tex = -tex;
			texturesheet = 0;
		}
		if (tex > 255) {
			texturesheet = tex/256;
			tex -= texturesheet*256;
		}
		index = tex;
		hasSubtypes = sub;
		name = n;
		itemClass = iCl;
	}

	private ItemRegistry(int lotex, int hitex, boolean sub, String n, Class <?extends Item> iCl) {
		if (lotex > hitex)
			throw new RegistrationException(RotaryCraft.instance, "Invalid item sprite registration for "+n+"! Backwards texture bounds?");
		texturesheet = 1;
		if (lotex < 0) {
			lotex = -lotex;
			hitex = -hitex;
			texturesheet = 0;
		}
		if (lotex > 255) {
			texturesheet = lotex/256;
			lotex -= texturesheet*256;
			hitex -= texturesheet*256;
		}
		index = lotex;
		maxindex = lotex;
		hasSubtypes = sub;
		name = n;
		itemClass = iCl;
	}

	public static final ItemRegistry[] itemList = ItemRegistry.values();

	public int getTextureIndex() {
		return index;
	}

	public static boolean isRegistered(ItemStack is) {
		return isRegistered(is.itemID);
	}

	public static boolean isRegistered(int id) {
		for (int i = 0; i < itemList.length; i++) {
			if (itemList[i].getID() == id)
				return true;
		}
		return false;
	}

	public static ItemRegistry getEntryByID(int id) {
		for (int i = 0; i < itemList.length; i++) {
			if (itemList[i].getID() == id)
				return itemList[i];
		}
		throw new RegistrationException(RotaryCraft.instance, "Item ID "+id+" was called to the item registry but does not exist there!");
	}

	public static ItemRegistry getEntry(ItemStack is) {
		if (is == null)
			return null;
		return getEntryByID(is.itemID);
	}

	public String getName(int dmg) {
		if (this.hasMultiValuedName())
			return this.getMultiValuedName(dmg);
		return name;
	}

	public String getBasicName() {
		if (name.startsWith("#"))
			return name.substring(1);
		return name;
	}

	public String getMultiValuedName(int dmg) {
		if (!this.hasMultiValuedName())
			throw new RuntimeException("Item "+name+" was called for a multi-name, yet does not have one!");
		if (this == SPRING)
			return "Wind Spring ("+String.format("%d", dmg)+" kJ)";
		if (this == BUCKET)
			return RotaryNames.bucketNames[dmg];
		if (this == RAILGUN)
			return "RailGun Ammunition ("+String.format("%d", (int)ReikaMathLibrary.intpow(2, dmg))+" kg)";
		if (this == SLIDE)
			return "Projector Slide "+String.format("%d", dmg);
		throw new RuntimeException("Item "+name+" was called for a multi-name, but it was not registered!");
	}

	public Constructor getConstructor() {
		try {
			if (this.isArmor())
				return itemClass.getConstructor(int.class, int.class, int.class); // ID, Armor render, Sprite index
			return itemClass.getConstructor(int.class, int.class); // ID, Sprite index
		}
		catch (NoSuchMethodException e) {
			throw new RegistrationException(RotaryCraft.instance, "Item Class "+itemClass.toString()+" does not have the specified constructor!");
		}
		catch (SecurityException e) {
			throw new RegistrationException(RotaryCraft.instance, "Item Class "+itemClass.toString()+" threw security exception!");
		}
	}

	public Item createInstance() {
		Constructor c = this.getConstructor();
		Item instance;
		try {
			if (this.isArmor())
				instance = (Item)(c.newInstance(RotaryConfig.itemids[this.ordinal()], this.getTextureIndex(), this.getArmorRender()));
			else
				instance = (Item)(c.newInstance(RotaryConfig.itemids[this.ordinal()], this.getTextureIndex()));
			return (instance.setUnlocalizedName(this.getUnlocName()));
		}
		catch (InstantiationException e) {
			throw new RegistrationException(RotaryCraft.instance, itemClass.toString()+" did not allow instantiation!");
		}
		catch (IllegalAccessException e) {
			throw new RegistrationException(RotaryCraft.instance, itemClass.toString()+" threw illegal access exception! (Nonpublic constructor)");
		}
		catch (IllegalArgumentException e) {
			throw new RegistrationException(RotaryCraft.instance, itemClass.toString()+" was given invalid parameters!");
		}
		catch (InvocationTargetException e) {
			throw new RegistrationException(RotaryCraft.instance, itemClass.toString()+" threw invocation target exception! Check Item ID conflicts!");
		}
	}

	private int getArmorRender() {
		if (!this.isArmor())
			throw new RegistrationException(RotaryCraft.instance, "Item "+name+" is not an armor yet was called for its render!");
		if (this == IOGOGGLES)
			return RotaryCraft.proxy.IOGoggles;
		if (this == NVG)
			return RotaryCraft.proxy.NVGoggles;
		if (this == NVH)
			return RotaryCraft.proxy.NVHelmet;
		throw new RegistrationException(RotaryCraft.instance, "Item "+name+" is an armor yet has no specified render!");
	}

	private String getUnlocName() {
		return ReikaJavaLibrary.stripSpaces(name).toLowerCase();
	}

	public int getID() {
		return this.getItemInstance().itemID;
	}

	public Item getItemInstance() {
		return RotaryCraft.basicItems[this.ordinal()];
	}

	public boolean hasMultiValuedName() {
		return name.startsWith("#");
	}

	public boolean isTool() {
		if (this.isBedrockTool())
			return true;
		return ItemRotaryTool.class.isAssignableFrom(itemClass);
	}

	public boolean isCharged() {
		return ItemChargedTool.class.isAssignableFrom(itemClass);
	}

	public boolean isBedrockTool() {
		return this == BEDPICK || this == BEDAXE || this == BEDSHOVEL;
	}

	public boolean isCreativeOnly() {
		if (this == DEBUG)
			return true;
		if (this == WORLDEDIT)
			return true;
		return false;
	}

	public int getTextureSheet() {
		return texturesheet;
	}

	public int getNumberMetadatas() {
		if (!hasSubtypes)
			return 1;
		if (this == SPRING)
			return 65536;
		if (this == RAILGUN)
			return 16;
		if (this == BUCKET)
			return RotaryNames.bucketNames.length;
		if (this == SLIDE)
			return 24;
		throw new RegistrationException(RotaryCraft.instance, "Item "+name+" has subtypes but the number was not specified!");
	}

	public boolean isArmor() {
		if (this == IOGOGGLES)
			return true;
		if (this == NVG)
			return true;
		if (this == NVH)
			return true;
		return false;
	}

	public ItemStack getCraftedProduct(int amt) {
		return new ItemStack(this.getID(), amt, 0);
	}

	public ItemStack getCraftedMetadataProduct(int amt, int meta) {
		return new ItemStack(this.getID(), amt, meta);
	}

	public ItemStack getStackOf() {
		return this.getCraftedProduct(1);
	}

	public ItemStack getStackOfMetadata(int meta) {
		return this.getCraftedMetadataProduct(1, meta);
	}

	public boolean overridesRightClick() {
		if (this == DEBUG)
			return true;
		if (this == METER)
			return true;
		if (this == SCREWDRIVER)
			return true;
		return false;
	}
}