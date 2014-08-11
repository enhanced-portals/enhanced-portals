package enhancedportals.client.gui;

import java.util.ArrayList;
import java.util.List;

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
    ArrayList<ElementManualTextButton> text_buttons=new ArrayList<ElementManualTextButton>();
    // Pages assigned to be triggered on next or prev.
    String next_page;
    String prev_page;
    // The page to go to by default (localization line).
    String start_page="ep3.manual.subject";
    // Text Format.
	int RED=0xFF0000;
	int DARK_GREY=0x222222;
	int LIGHT_GREY=0xBBBBBB;
	int GREY=0x444444;
	int BLACK=0x000000;
	// Char dimensions of a page.
	int chars_per_row=21;
	int rows_per_page=12;
	int header_rows=2;
	// Page margins.
    int page_margin=130;		// The amt of space a page spans.
    int content_margin=100;		// The amt of space the content can take up.
    int line_height=12;			// The height of each line.
    // String constants.
	String hr="_____________________";
	String loc_manual_string="manual.chapter";

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

    protected void writeChapterHeader(){
    	writeChapterHeader(ClientProxy.chapterNum);
    }
    protected void writeChapterHeader(int chapter_num){
    	String title=EnhancedPortals.localize(loc_manual_string+"."+chapter_num+".title").trim().toUpperCase();
    	getFontRenderer().drawString(title,15,15,RED);
    	drawSplitString(10,17,page_margin,hr,LIGHT_GREY);
    }
    // Use if you want all the pages.
    protected void drawChapterPages(int chapter_num,int chapter_page){
    	ArrayList<ArrayList<String>> pages=getChapterPages(chapter_num);
    	int left_margin=(page_margin-content_margin)/2;
    	// We display 2 pages at a time (called a spread) so we only need to get 1 number. We'll make it the even (left) side.
    	if(chapter_page%2==1){
    		chapter_page--;
    	}
    	int top_margin=15;
		// See if this is our first page.
		if(chapter_page==0){
			writeChapterHeader(chapter_num);
			top_margin=35;
		}
		// Left page.
		for(String line : pages.get(chapter_page)){
			getFontRenderer().drawString(line.trim(),left_margin,top_margin,DARK_GREY);
        	top_margin+=line_height;
		}
		top_margin=15;
		// Right page. Check if we have one first.
    	if((pages.size()-1)>chapter_page){
			for(String line : pages.get(chapter_page+1)){
				getFontRenderer().drawString(line.trim(),(page_margin+left_margin),top_margin,DARK_GREY);
	        	top_margin+=line_height;
			}
    	}
    }
    // Use if you want the page count.
    protected int countChapterPages(int chapter_num){
    	return getChapterPages(chapter_num).size();
    }
    protected List<String> parseStringByWidth(String string,int line_char_count){
    	List<String> lines=new ArrayList<String>();
    	String sub;
    	do{
    		// Check that there are at least line_char_count # of chars left in string.
    		if(string.length()<line_char_count){
    			sub=string;
    		// Otherwise, let's cut that amount out and trim it for split words.
    		}else{
    			sub=string.substring(0,line_char_count);
    			// Check if we just cut through a word.
    			if(string.length()>line_char_count+1){
    				if((string.charAt(line_char_count-1)!=' ')&&(string.charAt(line_char_count)!=' ')){
	    				// Check if the whole line is one large word.
	    				if(!(sub.lastIndexOf(' ')<0)){
	    					sub=sub.substring(0,sub.lastIndexOf(' '));
	    				}
    				}
    			}
    		}
			string=string.substring(sub.length());
			lines.add(sub);
    	}while(!string.isEmpty());
    	return lines;
    }
    protected ArrayList<ArrayList<String>> getChapterPages(int chapter_num){
    	ArrayList<ArrayList<String>> pages=new ArrayList<ArrayList<String>>();
    	ArrayList<String> paragraphs=new ArrayList<String>();
		String loc;
		int iterator=0;
		// Gather all paragraphs for this Chapter and put them in 'paragraphs'.
    	do{
    		// Set up some localization things.
    		loc=loc_manual_string+"."+chapter_num+".p.";
    		String paragraph=EnhancedPortals.localize(loc+iterator);
    		iterator++;
    		// Add to our array.
    		paragraphs.add(paragraph);
    	// Loop if we have another paragraph after this.
    	}while(ClientProxy.locExists(loc+iterator));
    	// Process the paragraphs and turn them into pages.
    	while(!paragraphs.isEmpty()){
    		ArrayList<String> page=new ArrayList<String>();
    		// Determine the amount of rows we need to grab per page.
    		int page_rows=(pages.size()==0?(rows_per_page-header_rows):rows_per_page);
    		// Fill up the page array as long as the page array isn't full and we still have paragraphs left.
    		while((page.size()<page_rows)&&!paragraphs.isEmpty()){
    			ArrayList<String> paragraph_lines=new ArrayList<String>();
    			paragraph_lines.addAll(parseStringByWidth(paragraphs.get(0),chars_per_row));
    			// We need more lines to fill this page.
    			if(paragraph_lines.size()<=(page_rows-page.size())){
    				page=addListToArrayList(page,paragraph_lines);
    				// Check if we need to include a separator line between paragraphs.
    				if(paragraph_lines.size()!=(page_rows-page.size())){
    					page.add("");
    				}
    				paragraphs.remove(0);
    			// Otherwise we are over.
    			}else{
    				int remove_amt_of_lines=(page_rows-page.size());
    				// Add the lines to 'page' that we need to fill it up.
    				page=addListToArrayList(page,paragraph_lines.subList(0,remove_amt_of_lines));
    				// Then delete them from our paragraph_lines.
    				paragraph_lines.subList(0,remove_amt_of_lines).clear();
					paragraphs.remove(0);
					// Add the remaining paragraph back to paragraphs.
					paragraphs.add(0,mergeToString(paragraph_lines));
    			}
    		}
    		pages.add(page);
    	}
    	return pages;
    }
    protected String mergeToString(ArrayList<String> a){
    	List<String> l=new ArrayList<String>();
    	l.addAll(a);
    	return mergeToString(l);
    }
    protected String mergeToString(List<String> list){
		String string="";
		for(String i : list){
			string+=i;
		}
		return string;
    }
    protected ArrayList<String> addListToArrayList(ArrayList<String> a,List<String> l){
    	for(String i : l){
    		a.add(i);
    	}
    	return a;
    }
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        hideItemLinks();
        
        // Check to see where we are in the Manual:
        // Main Title page.
        if(ClientProxy.manualEntry.equals("subject")){
        	hideCraftingTable();
        	// drawSplitString(<right>,<down>,<width>,<text>,<color>)
        	drawSplitString(160,70,page_margin,"ENHANCED",RED);
        	drawSplitString(210,70,page_margin,"PORTALS",DARK_GREY);
            getFontRenderer().drawSplitString(EnhancedPortals.localize("manual.subject.sub").toLowerCase(),160,80,page_margin,LIGHT_GREY);
        // Table of Contents.
        }else if(ClientProxy.manualEntry.equals("contents")){
        	hideCraftingTable();
        	drawSplitString(15,20,page_margin,"> "+EnhancedPortals.localize("manual.table_of_contents.title").toUpperCase()+" <",RED);
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
        // Chapters.
        }else if(ClientProxy.manualEntry.equals("chapter")){
        	hideCraftingTable();
        	drawChapterPages(ClientProxy.chapterNum,ClientProxy.chapterPage);
        // Item Gallery for each chapter.
        }else if(ClientProxy.manualEntry.equals("gallery")){
        	hideCraftingTable();
        	String title=EnhancedPortals.localize("manual.gallery.title");
        	ArrayList<String> items=new ArrayList<String>();
        	// Draw the header.
        	getFontRenderer().drawString(title.toUpperCase(),40,15,LIGHT_GREY);
        	getFontRenderer().drawString("==========",45,25,DARK_GREY);
        	switch(ClientProxy.chapterNum){
	    		case 2:
	    			items.add("dbs");
	    			items.add("wrench");
	    			break;
	    		case 3:
	    			items.add("frame0");		// Portal Frame
	    			items.add("dbs");
	    			items.add("location_card");
	    			items.add("wrench");
	    			items.add("frame1");		// Frame Controller
	    			items.add("frame3");		// Network Interface
	    			break;
	    		case 4:
	    			items.add("frame2");		// Redstone Interface
	    			break;
	    		case 5:
	    			items.add("frame4");		// Dialling Device
	    			break;
	    		case 6:
	    			items.add("nanobrush");
	    			items.add("frame6");		// Module Manipulator
	    			items.add("frame7");		// Fuild Transportation Module
	    			items.add("frame8");		// Item Transportation Module
	    			items.add("frame9");		// Energy Transportation Module
	    			break;
        	}
        	drawItemLinks(items);
        // Otherwise we are viewing an Item page.
        }else{
            ItemStack[] stacks = ClientProxy.getCraftingRecipeForManualEntry();
            craftingGrid.setVisible(stacks != null);
            craftingGrid.setItems(stacks);
            addElement(craftingGrid);
            // Setup some variables to output the item info.
            int left_margin=15;
            int top_margin=15;
            String loc_entry="manual."+ClientProxy.manualEntry;
            top_margin+=drawSplitString(left_margin,top_margin,content_margin,EnhancedPortals.localize(loc_entry+".title").toUpperCase(),RED);
            // Check if subtitles exist.
            if(ClientProxy.locExists(loc_entry+".subtitle")){
            	getFontRenderer().drawSplitString(EnhancedPortals.localize(loc_entry+".subtitle"),left_margin,(top_margin),content_margin,LIGHT_GREY);
            }
            // Right page.
            top_margin=15;
            getFontRenderer().drawSplitString(EnhancedPortals.localize(loc_entry+".info"),(page_margin+left_margin),top_margin,content_margin,DARK_GREY);
        }
        super.drawGuiContainerForegroundLayer(par1, par2);
    }
    protected void hideCraftingTable(){
    	if(craftingGrid!=null){
	    	// Hide the crafting grid and item holders.
	        craftingGrid.setVisible(false);
	        craftingGrid.setItems(null);
    	}
    }
    protected void hideItemLinks(){
    	if(!text_buttons.isEmpty()){
	    	for(ElementManualTextButton link : text_buttons){
	    		link.setVisible(false);
	    	}
    	}
    }
    protected void drawItemLinks(ArrayList<String> items){
    	int top_margin=40;
    	int left_margin=15;
    	for(String item : items){
    		// If it's blank, means we want a spacer.
    		if(!item.isEmpty()){
    			ElementManualTextButton link=new ElementManualTextButton(this,left_margin,top_margin,item);
    	        link.setVisible(true);
    	        addElement(link);
    			text_buttons.add(link);
    		}
			top_margin+=line_height;
    	}
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton){
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if((mouseButton == 4)&&nextPage(true)){
            nextPage();
            return;
        }else if((mouseButton == 3)&&prevPage(true)){
            prevPage();
            return;
        }else if (mouseY >= guiTop + CONTAINER_SIZE + 3 && mouseY < guiTop + CONTAINER_SIZE + 13){
            if((mouseX >= guiLeft + CONTAINER_WIDTH - 23 && mouseX < guiLeft + CONTAINER_WIDTH - 5)&&nextPage(true)){
                nextPage();
                return;
            }else if((mouseX >= guiLeft + 5 && mouseX < guiLeft + 23)&&prevPage(true)){
                prevPage();
                return;
            }else if (mouseX >= guiLeft + (xSize / 2 - 10) && mouseX < guiLeft + (xSize / 2 - 10) + 21){
            	// Check if we need to link back to the Table of Contents or the last page we were viewing.
                if(ClientProxy.manualEntry.equals("chapter")||ClientProxy.manualEntry.equals("gallery")){
                	changeEntry("contents");
                }else if(!ClientProxy.manualEntry.equals("subject")&&!ClientProxy.manualEntry.equals("contents")){
                	changeEntry("gallery");
                }
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
    		int total_chapter_pages=countChapterPages(ClientProxy.chapterNum);
    		// Check if the current page is not the last page.
    		if(total_chapter_pages>(ClientProxy.chapterPage+2)){
        		if(is_next){
        			return true;
        		}else{
	    			ClientProxy.chapterPage+=2;
		    		changeEntry("chapter");
        		}
    		}else{
    			// Check if this chapter has a term Gallery.
    			if(ClientProxy.chapterNum>1){
            		if(is_next){
            			return true;
            		}else{
	    	    		changeEntry("gallery");
            		}
            	// Otherwise go to the next chapter.
    			}else if(ClientProxy.manualChapterExists(ClientProxy.chapterNum+1)){
            		if(is_next){
            			return true;
            		}else{
	    				ClientProxy.chapterNum++;
	    				ClientProxy.chapterPage=0;
	    	    		changeEntry("chapter");
            		}
    			}
    		}
    	}else if(ClientProxy.manualEntry.equals("gallery")){
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
    		if(!(ClientProxy.chapterPage<=1)){
        		if(is_prev){
        			return true;
        		}else{
	    			ClientProxy.chapterPage-=2;
		    		changeEntry("chapter");
        		}
    		}else{
    			// Check if there is a next chapter.
    			if(ClientProxy.manualChapterExists(ClientProxy.chapterNum-1)){
            		if(is_prev){
            			return true;
            		}else{
	    				ClientProxy.chapterNum--;
	    				ClientProxy.chapterPage=countChapterPages(ClientProxy.chapterNum)-1;
	    				// Check if the previous chapter has a Gallery.
	    				if(ClientProxy.chapterNum>1){
	    					changeEntry("gallery");
	    				}else{
	    					changeEntry("chapter");
	    				}
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
    	}else if(ClientProxy.manualEntry.equals("gallery")){
    		if(is_prev){
    			return true;
    		}else{
				ClientProxy.chapterPage=countChapterPages(ClientProxy.chapterNum)-1;
	    		changeEntry("chapter");
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

        // Initiate the crafting grid for the item pages.
        craftingGrid = new ElementManualCraftingGrid(this,(70-33),(90-33),null);
    }

    public void pageChanged()
    {

    }
}
