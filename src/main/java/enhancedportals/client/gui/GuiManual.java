package enhancedportals.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import enhancedportals.EnhancedPortals;
import enhancedportals.client.gui.elements.ElementManualCraftingGrid;
import enhancedportals.client.gui.elements.ElementManualTextButton;
import enhancedportals.inventory.ContainerManual;
import enhancedportals.network.ClientProxy;

public class GuiManual extends BaseGui
{
    public static final int CONTAINER_SIZE = 180, CONTAINER_WIDTH = 279;
    static ResourceLocation textureB = new ResourceLocation("enhancedportals", "textures/gui/manualB.png");
    ElementManualCraftingGrid craftingGrid;
    ElementManualTextButton[] textButtons;
    // Pages assigned to be triggered on next or prev.
    String next_page;
    String prev_page;
    // The page to go to by default (localization line).
    String start_page="ep3.manual.subject";

    public GuiManual(EntityPlayer p)
    {
        super(new ContainerManual(p.inventory), CONTAINER_SIZE);
        xSize = CONTAINER_WIDTH;
        setHidePlayerInventory();
        texture = new ResourceLocation("enhancedportals", "textures/gui/manualA.png");
    }

    @Override
    // Draws the next and prev page icons, and "back" icon.
    protected void drawBackgroundTexture()
    {
        mc.renderEngine.bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, 140, ySize);
        mc.renderEngine.bindTexture(textureB);
        drawTexturedModalRect(guiLeft + 140, guiTop, 0, 0, 139, ySize);

        boolean mouseHeight = guiTop + mouseY >= guiTop + CONTAINER_SIZE + 3 && guiTop + mouseY < guiTop + CONTAINER_SIZE + 13;
        boolean rightButton = mouseHeight && guiLeft + mouseX >= guiLeft + CONTAINER_WIDTH - 23 && guiLeft + mouseX < guiLeft + CONTAINER_WIDTH - 5;
        boolean leftButton = mouseHeight && guiLeft + mouseX >= guiLeft + 5 && guiLeft + mouseX < guiLeft + 23;
        boolean middleButton = mouseHeight && guiLeft + mouseX >= guiLeft + (xSize / 2 - 10) && guiLeft + mouseX < guiLeft + (xSize / 2 - 10) + 21;

        // Draw "next page" button.
        if (nextPage(true)){
            drawTexturedModalRect(guiLeft + CONTAINER_WIDTH - 23, guiTop + CONTAINER_SIZE + 3, rightButton ? 23 : 0, 233, 18, 10);
        }
        // Draw "prev page" button.
        if (prevPage(true)){
            drawTexturedModalRect(guiLeft + 5, guiTop + CONTAINER_SIZE + 3, leftButton ? 23 : 0, 246, 18, 10);
        }
        // Draw "back to" button.
        if (!(ClientProxy.manualEntry.equals("subject")||ClientProxy.manualEntry.equals("contents"))){
            drawTexturedModalRect(guiLeft + (xSize / 2 - 10), guiTop + CONTAINER_SIZE + 3, middleButton ? 21 : 0, 222, 21, 10);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
    	int RED=0xFF0000;
    	int DARK_GREY=0x222222;
    	int LIGHT_GREY=0xBBBBBB;
    	int GREY=0x444444;
    	int BLACK=0x000000;
        int page_margin=130;
        // Used when you want to wrap
        int content_margin=120;

        if(ClientProxy.manualEntry.equals("subject")){
        	// drawSplitString(<right>,<down>,<width>,<text>,<color>)
        	drawSplitString(160,70,page_margin,"ENHANCED",RED);
        	drawSplitString(210,70,page_margin,"PORTALS",DARK_GREY);
            getFontRenderer().drawSplitString(EnhancedPortals.localize("manual.subject.sub"),160,80,page_margin,LIGHT_GREY);
        }else if(ClientProxy.manualEntry.equals("contents")){
        	drawSplitString(15,20,page_margin,"> "+EnhancedPortals.localize("manual.table_of_contents.title")+" <",RED);
        	drawSplitString(17,35,page_margin,":",RED);
        	drawSplitString(20,35,page_margin,EnhancedPortals.localize("manual.chapter.0.title"),GREY);
        	drawSplitString(17,50,page_margin,":",RED);
        	drawSplitString(20,50,page_margin,EnhancedPortals.localize("manual.chapter.1.title"),GREY);
        	drawSplitString(17,65,page_margin,":",RED);
        	drawSplitString(20,65,120,EnhancedPortals.localize("manual.chapter.2.title"),GREY);
        	drawSplitString(17,80,page_margin,":",RED);
        	drawSplitString(20,80,120,EnhancedPortals.localize("manual.chapter.3.title"),GREY);
        	drawSplitString(17,95,page_margin,":",RED);
        	drawSplitString(20,95,120,EnhancedPortals.localize("manual.chapter.4.title"),GREY);
        	drawSplitString(17,110,page_margin,":",RED);
        	drawSplitString(20,110,page_margin,EnhancedPortals.localize("manual.chapter.5.title"),GREY);
        	drawSplitString(17,125,page_margin,":",RED);
        	drawSplitString(20,125,page_margin,EnhancedPortals.localize("manual.chapter.6.title"),GREY);
        }else if(ClientProxy.manualEntry.equals("chapter")){
        	String loc_manual_string="manual.chapter";
        	String hr="_____________________";
        	switch(ClientProxy.chapterNum){
        		case 0:
                	drawSplitString(15,20,page_margin,EnhancedPortals.localize(loc_manual_string+"."+ClientProxy.chapterNum+".title"),RED);
                	drawSplitString(10,21,page_margin,hr,LIGHT_GREY);
                	drawSplitString(17,40,content_margin,EnhancedPortals.localize(loc_manual_string+"."+ClientProxy.chapterNum+".page.0"),DARK_GREY);
        			break;
        		case 1:
                	drawSplitString(15,20,page_margin,EnhancedPortals.localize(loc_manual_string+"."+ClientProxy.chapterNum+".title"),RED) ;
                	drawSplitString(10,21,page_margin,hr,LIGHT_GREY);
        			break;
        		case 2:
                	drawSplitString(15,20,page_margin,EnhancedPortals.localize(loc_manual_string+"."+ClientProxy.chapterNum+".title"),RED);
                	drawSplitString(10,21,page_margin,hr,LIGHT_GREY);
        			break;
        		case 3:
                	drawSplitString(15,20,page_margin,EnhancedPortals.localize(loc_manual_string+"."+ClientProxy.chapterNum+".title"),RED);
                	drawSplitString(10,21,page_margin,hr,LIGHT_GREY);
        			break;
        		case 4:
                	drawSplitString(15,20,page_margin,EnhancedPortals.localize(loc_manual_string+"."+ClientProxy.chapterNum+".title"),RED);
                	drawSplitString(10,21,page_margin,hr,LIGHT_GREY);
        			break;
        		case 5:
                	drawSplitString(15,20,page_margin,EnhancedPortals.localize(loc_manual_string+"."+ClientProxy.chapterNum+".title"),RED);
                	drawSplitString(10,21,page_margin,hr,LIGHT_GREY);
        			break;
        		case 6:
                	drawSplitString(15,20,page_margin,EnhancedPortals.localize(loc_manual_string+"."+ClientProxy.chapterNum+".title"),RED);
                	drawSplitString(10,21,page_margin,hr,LIGHT_GREY);
        			break;
        	}
        }
        /*
        if (ClientProxy.manualEntry.equals("main"))
        {
            if (ClientProxy.manualPage == 0)
            {
                int offset = drawSplitString(17, 12, pageWidth, EnhancedPortals.localize("manual." + ClientProxy.manualEntry + ".title"), 0x000000);
                getFontRenderer().drawSplitString(EnhancedPortals.localize("manual." + ClientProxy.manualEntry + ".page.0"), 17, 15 + offset, pageWidth, 0x000000);
            }
        }
        else if (ClientProxy.manualPage == 0)
        {
            int offset = drawSplitString(17, 12, pageWidth, EnhancedPortals.localize("manual." + ClientProxy.manualEntry + ".title"), 0x000000);
            String sub = EnhancedPortals.localize("manual." + ClientProxy.manualEntry + ".subtitle");

            if (!sub.contains(".subtitle"))
            {
                getFontRenderer().drawSplitString(sub, 17, offset + 13, pageWidth, 0x888888);
            }

            getFontRenderer().drawSplitString(EnhancedPortals.localize("manual." + ClientProxy.manualEntry + ".page.0"), 147, 12, pageWidth, 0x000000);
        }
        else
        {
            getFontRenderer().drawSplitString(EnhancedPortals.localize("manual." + ClientProxy.manualEntry + ".page." + ClientProxy.manualPage), 15, 12, pageWidth, 0x000000);

            if (ClientProxy.manualEntryHasPage(ClientProxy.manualPage + 1))
            {
                getFontRenderer().drawSplitString(EnhancedPortals.localize("manual." + ClientProxy.manualEntry + ".page." + (ClientProxy.manualPage + 1)), 148, 12, pageWidth, 0x000000);
            }
        }
        */
        super.drawGuiContainerForegroundLayer(par1, par2);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseButton == 4)
        {
            nextPage();
            return;
        }
        else if (mouseButton == 3)
        {
            prevPage();
            return;
        }
        else if (mouseY >= guiTop + CONTAINER_SIZE + 3 && mouseY < guiTop + CONTAINER_SIZE + 13)
        {
            if (mouseX >= guiLeft + CONTAINER_WIDTH - 23 && mouseX < guiLeft + CONTAINER_WIDTH - 5)
            {
                nextPage();
                return;
            }
            else if (mouseX >= guiLeft + 5 && mouseX < guiLeft + 23)
            {
                prevPage();
                return;
            }
            else if (mouseX >= guiLeft + (xSize / 2 - 10) && mouseX < guiLeft + (xSize / 2 - 10) + 21)
            {
                changeEntry("contents");
                return;
            }
        }
    }

    void nextPage(){
    	nextPage(false);
    }
    void prevPage(){
    	prevPage(false);
    }
    boolean nextPage(boolean is_next){
    	// -> on Subject page.
    	if(ClientProxy.manualEntry.equals("subject")){
    		if(is_next){
    			return true;
    		}else{
	    		// Trigger the page change.
    			changeEntry("contents");
    		}
    	// -> on Table of Contents.
    	}else if(ClientProxy.manualEntry.equals("contents")){
    		if(is_next){
    			return true;
    		}else{
				ClientProxy.chapterNum=0;
				ClientProxy.chapterPage=0;
	    		changeEntry("chapter");
    		}
    	}else if(ClientProxy.manualEntry.equals("chapter")){
    		// Check if the current page is not the last page.
    		if(ClientProxy.manualChapterPageExists(ClientProxy.chapterNum,ClientProxy.chapterPage+1)){
        		if(is_next){
        			return true;
        		}else{
	    			ClientProxy.chapterPage++;
		    		changeEntry("chapter");
        		}
    		}else{
    			// Check if there is a next chapter.
    			if(ClientProxy.manualChapterExists(ClientProxy.chapterNum+1)){
            		if(is_next){
            			return true;
            		}else{
	    				ClientProxy.chapterNum++;
	    				ClientProxy.chapterPage=0;
	    	    		changeEntry("chapter");
            		}
    			}
    		}
    	}
    	return false;
    }
    boolean prevPage(boolean is_prev){
    	// <- on Table of Contents.
    	if(ClientProxy.manualEntry.equals("contents")){
    		if(is_prev){
    			return true;
    		}else{
	    		changeEntry("subject");
    		}
        // <- on a Chapter page.
    	}else if(ClientProxy.manualEntry.equals("chapter")){
    		// Check if the current page is not the last page.
    		if(ClientProxy.manualChapterPageExists(ClientProxy.chapterNum,ClientProxy.chapterPage-1)){
        		if(is_prev){
        			return true;
        		}else{
	    			ClientProxy.chapterPage--;
		    		changeEntry("chapter");
        		}
    		}else{
    			// Check if there is a next chapter.
    			if(ClientProxy.manualChapterExists(ClientProxy.chapterNum-1)){
            		if(is_prev){
            			return true;
            		}else{
	    				ClientProxy.chapterNum--;
	    				ClientProxy.chapterPage=ClientProxy.manualChapterLastPage(ClientProxy.chapterNum);
	    	    		changeEntry("chapter");
            		}
    			}else{
            		if(is_prev){
            			return true;
            		}else{
	    				ClientProxy.chapterNum=0;
	    				ClientProxy.chapterPage=0;
	    	    		changeEntry("contents");
            		}
    			}
    		}
    	}
    	return false;
    }

    void changeEntry(String e)
    {
        ClientProxy.manualChangeEntry(e);
        pageChanged();
    }

    @Override
    public void initGui()
    {
        super.initGui();

        textButtons = new ElementManualTextButton[30];
        for (int i = 0; i < textButtons.length; i++)
        {
        	GuiManual gui=this;
        	int x=(i<15?17:147);
        	int y=15+(i>=15?(i-15)*10:i*10);
        	String mEntry="main";
            textButtons[i] = new ElementManualTextButton(gui,x,y,mEntry);
            textButtons[i].setVisible(false);
            addElement(textButtons[i]);
        }

        craftingGrid = new ElementManualCraftingGrid(this, 70 - 33, 90 - 33, null);
        pageChanged();
        addElement(craftingGrid);
    }

    public void pageChanged()
    {
    	// Hide the crafting grid and item holders.
        craftingGrid.setVisible(false);
        craftingGrid.setItems(null);
    	/*
    	// ep3.manual.<section (m[2])>.<final|sub|#>.<title|#>
    	String[] m=loc_page.split(".");
    	if(m!=null&&m.length>2){
	    	if(m[2]=="subject"){
	    		nextPage("ep.manual.chapter.0");
	    		// Page 1
	    		textButtons[0].updateStr("");
        		textButtons[1].updateStr("");
        		textButtons[2].updateStr("");
        		textButtons[3].updateStr("");
        		textButtons[4].updateStr(" +-----------------+ ");
        		textButtons[5].updateStr("ep3.manual.subject.title",true);
        		textButtons[6].updateStr(" +-----------------+ ");
	    		textButtons[7].updateStr("ep3.manual.subject.sub",true);
	    		textButtons[8].updateStr("");
	    		textButtons[9].updateStr("");
	    		textButtons[10].updateStr("");
	    		textButtons[11].updateStr("");
	    		textButtons[12].updateStr("");
	    		textButtons[13].updateStr("");
	    		textButtons[14].updateStr("");
	    		// Page 2
	    		textButtons[15].updateStr("+ TABLE OF CONTENTS +");
	    		textButtons[16].updateStr("");
	    		textButtons[17].updateStr("0001 :: "+EnhancedPortals.localize("ep3.manual.chapter.0.title"));
	    		textButtons[18].updateStr("");
	    		textButtons[19].updateStr("0010 :: "+EnhancedPortals.localize("ep3.manual.chapter.1.title"));
	    		textButtons[20].updateStr("");
	    		textButtons[21].updateStr("0011 :: "+EnhancedPortals.localize("ep3.manual.chapter.2.title"));
	    		textButtons[22].updateStr("");
	    		textButtons[23].updateStr("0100 :: "+EnhancedPortals.localize("ep3.manual.chapter.3.title"));
	    		textButtons[24].updateStr("");
	    		textButtons[25].updateStr("0101 :: "+EnhancedPortals.localize("ep3.manual.chapter.4.title"));
	    		textButtons[26].updateStr("");
	    		textButtons[27].updateStr("0110 :: "+EnhancedPortals.localize("ep3.manual.chapter.5.title"));
	    		textButtons[28].updateStr("");
	    		textButtons[29].updateStr("0111 :: "+EnhancedPortals.localize("ep3.manual.chapter.6.title"));
	    	}else if(m[2]=="chapter"){
	    	}
    	}
    	// Check if these are main entries, or normal text, not item recipes.
    	if (ClientProxy.manualEntry.equals("main")){
        	// Hide the crafting grid and item holders.
            craftingGrid.setVisible(false);
            craftingGrid.setItems(null);

            // Each page only holds 15 lines, so iterate through these 15.
            for (int i = 0; i < textButtons.length; i++)
            {
                textButtons[i].setVisible(ClientProxy.manualPage == 0 && i < 15 ? false : true);
            }

        	// Gallery item list. Localization vars that don't end with main.page.#
            switch (ClientProxy.manualPage)
            {
            	case "subPage":
            		textButtons[0].updateEntry();
            		textButtons[1
            		textButtons[2
            		textButtons[3
            		textButtons[4
            		textButtons[5
            		textButtons[6
                case 0:
                    textButtons[15].updateEntry("portal");
                    textButtons[16].updateEntry("frame0");
                    textButtons[17].updateEntry("frame1");
                    textButtons[18].updateEntry("frame2");
                    textButtons[19].updateEntry("frame3");
                    textButtons[20].updateEntry("frame4");
                    textButtons[21].updateEntry("frame5");
                    textButtons[22].updateEntry("frame6");
                    textButtons[23].updateEntry("frame7");
                    textButtons[24].updateEntry("frame8");
                    textButtons[25].updateEntry("frame9");
                    textButtons[26].updateEntry("dbs");
                    textButtons[27].updateEntry("decorStabilizer");
                    textButtons[28].updateEntry("decorBorderedQuartz");
                    textButtons[29].updateEntry("dbsEmpty");
                    break;
                case 1:
                    textButtons[0].updateEntry("wrench");
                    textButtons[1].updateEntry("nanobrush");
                    textButtons[2].updateEntry("glasses");
                    textButtons[3].updateEntry("location_card");
                    textButtons[4].updateEntry(null);
                    textButtons[5].updateEntry("blank_module");
                    textButtons[6].updateEntry("module0");
                    textButtons[7].updateEntry("module1");
                    textButtons[8].updateEntry("module2");
                    textButtons[9].updateEntry("module3");
                    textButtons[10].updateEntry("module4");
                    textButtons[11].updateEntry("module5");
                    textButtons[12].updateEntry("module6");
                    textButtons[13].updateEntry("module7");
                    textButtons[14].updateEntry(null);
                    textButtons[15].updateEntry("blank_upgrade");
                    textButtons[16].updateEntry("upgrade0");
                    textButtons[17].updateEntry("upgrade1");
                    textButtons[18].updateEntry("upgrade2");
                    textButtons[19].updateEntry("upgrade3");
                    textButtons[20].updateEntry("upgrade4");
                    textButtons[21].updateEntry("upgrade5");
                    textButtons[22].updateEntry("upgrade6");
                    textButtons[23].updateEntry("upgrade7");
                    break;
                default:
                    break;
            }
        }else{
            for(int i = 0; i < textButtons.length; i++){
                textButtons[i].setVisible(false);
            }

            ItemStack[] stacks = ClientProxy.getCraftingRecipeForManualEntry();
            craftingGrid.setVisible(stacks != null);
            craftingGrid.setItems(stacks);
        }
        */
    }
}
