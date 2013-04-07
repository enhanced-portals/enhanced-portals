package alz.mods.enhancedportals.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ChatAllowedCharacters;

import org.lwjgl.opengl.GL11;

public class GuiImprovedTextBox extends GuiTextField
{
	/**
	 * Have the font renderer from GuiScreen to render the textbox text into the
	 * screen.
	 */
	private final FontRenderer fontRenderer;
	private int xPos;
	private int yPos;

	/** The width of this text field. */
	private final int width;
	private final int height;

	/** Have the current text beign edited on the textbox. */
	private String text = "";
	private int maxStringLength = 32;
	private int cursorCounter;
	private boolean enableBackgroundDrawing = true;

	/**
	 * if true the textbox can lose focus by clicking elsewhere on the screen
	 */
	private boolean canLoseFocus = true;

	/**
	 * If this value is true along isEnabled, keyTyped will process the keys.
	 */
	private boolean isFocused = false;

	/**
	 * If this value is true along isFocused, keyTyped will process the keys.
	 */
	private boolean isEnabled = true;

	/**
	 * The current character index that should be used as start of the rendered
	 * text.
	 */
	private int lineScrollOffset = 0;
	private int cursorPosition = 0;

	/** other selection position, maybe the same as the cursor */
	private int selectionEnd = 0;
	private int enabledColor = 14737632;
	private int disabledColor = 7368816;

	/** True if this textbox is visible */
	private boolean visible = true, intsOnly, CanFocus;
	private int drawX, drawY;
	private String DummyText;

	public GuiImprovedTextBox(FontRenderer par1FontRenderer, int par2, int par3, int par4, int par5, int maxlength, boolean intsonly, boolean canFocus)
	{
		super(par1FontRenderer, par2, par3, par4, par5);

		xPos = drawX = par2;
		yPos = drawY = par3;
		width = par4;
		height = par5;
		intsOnly = intsonly;
		CanFocus = canFocus;
		fontRenderer = par1FontRenderer;

		setMaxStringLength(maxlength);
	}

	public GuiImprovedTextBox(FontRenderer par1FontRenderer, int par2, int par3, int par4, int par5, int maxlength, boolean intsonly, boolean canFocus, int drawx, int drawy)
	{
		super(par1FontRenderer, par2, par3, par4, par5);

		xPos = par2;
		yPos = par3;
		width = par4;
		height = par5;
		intsOnly = intsonly;
		CanFocus = canFocus;
		drawX = drawx;
		drawY = drawy;
		fontRenderer = par1FontRenderer;

		setMaxStringLength(maxlength);
	}

	public GuiImprovedTextBox(FontRenderer par1FontRenderer, int par2, int par3, int par4, int par5, int maxlength, boolean intsonly, boolean canFocus, int drawx, int drawy, String dummyText)
	{
		super(par1FontRenderer, par2, par3, par4, par5);

		xPos = par2;
		yPos = par3;
		width = par4;
		height = par5;
		intsOnly = intsonly;
		CanFocus = canFocus;
		drawX = drawx;
		drawY = drawy;
		fontRenderer = par1FontRenderer;
		DummyText = text = dummyText;

		setMaxStringLength(maxlength);
	}

	/**
	 * Increments the cursor counter
	 */
	@Override
	public void updateCursorCounter()
	{
		++cursorCounter;
	}

	/**
	 * Sets the text of the textbox.
	 */
	@Override
	public void setText(String par1Str)
	{
		if (par1Str.length() > maxStringLength)
		{
			text = par1Str.substring(0, maxStringLength);
		}
		else
		{
			text = par1Str;
		}

		this.setCursorPositionEnd();
	}

	/**
	 * Returns the text beign edited on the textbox.
	 */
	@Override
	public String getText()
	{
		return text;
	}

	/**
	 * @return returns the text between the cursor and selectionEnd
	 */
	@Override
	public String getSelectedtext()
	{
		int i = cursorPosition < selectionEnd ? cursorPosition : selectionEnd;
		int j = cursorPosition < selectionEnd ? selectionEnd : cursorPosition;
		return text.substring(i, j);
	}

	/**
	 * replaces selected text, or inserts text at the position on the cursor
	 */
	@Override
	public void writeText(String par1Str)
	{
		String s1 = "";
		String s2 = ChatAllowedCharacters.filerAllowedCharacters(par1Str);
		int i = cursorPosition < selectionEnd ? cursorPosition : selectionEnd;
		int j = cursorPosition < selectionEnd ? selectionEnd : cursorPosition;
		int k = maxStringLength - text.length() - (i - selectionEnd);

		if (text.length() > 0)
		{
			s1 = s1 + text.substring(0, i);
		}

		int l;

		if (k < s2.length())
		{
			s1 = s1 + s2.substring(0, k);
			l = k;
		}
		else
		{
			s1 = s1 + s2;
			l = s2.length();
		}

		if (text.length() > 0 && j < text.length())
		{
			s1 = s1 + text.substring(j);
		}

		text = s1;
		this.moveCursorBy(i - selectionEnd + l);
	}

	/**
	 * Deletes the specified number of words starting at the cursor position.
	 * Negative numbers will delete words left of the cursor.
	 */
	@Override
	public void deleteWords(int par1)
	{
		if (text.length() != 0)
		{
			if (selectionEnd != cursorPosition)
			{
				this.writeText("");
			}
			else
			{
				this.deleteFromCursor(this.getNthWordFromCursor(par1) - cursorPosition);
			}
		}
	}

	/**
	 * delete the selected text, otherwsie deletes characters from either side
	 * of the cursor. params: delete num
	 */
	@Override
	public void deleteFromCursor(int par1)
	{
		if (text.length() != 0)
		{
			if (selectionEnd != cursorPosition)
			{
				this.writeText("");
			}
			else
			{
				boolean flag = par1 < 0;
				int j = flag ? cursorPosition + par1 : cursorPosition;
				int k = flag ? cursorPosition : cursorPosition + par1;
				String s = "";

				if (j >= 0)
				{
					s = text.substring(0, j);
				}

				if (k < text.length())
				{
					s = s + text.substring(k);
				}

				text = s;

				if (flag)
				{
					this.moveCursorBy(par1);
				}
			}
		}
	}

	/**
	 * see @getNthNextWordFromPos() params: N, position
	 */
	@Override
	public int getNthWordFromCursor(int par1)
	{
		return this.getNthWordFromPos(par1, this.getCursorPosition());
	}

	/**
	 * gets the position of the nth word. N may be negative, then it looks
	 * backwards. params: N, position
	 */
	@Override
	public int getNthWordFromPos(int par1, int par2)
	{
		return this.func_73798_a(par1, this.getCursorPosition(), true);
	}

	@Override
	public int func_73798_a(int par1, int par2, boolean par3)
	{
		int k = par2;
		boolean flag1 = par1 < 0;
		int l = Math.abs(par1);

		for (int i1 = 0; i1 < l; ++i1)
		{
			if (flag1)
			{
				while (par3 && k > 0 && text.charAt(k - 1) == 32)
				{
					--k;
				}

				while (k > 0 && text.charAt(k - 1) != 32)
				{
					--k;
				}
			}
			else
			{
				int j1 = text.length();
				k = text.indexOf(32, k);

				if (k == -1)
				{
					k = j1;
				}
				else
				{
					while (par3 && k < j1 && text.charAt(k) == 32)
					{
						++k;
					}
				}
			}
		}

		return k;
	}

	/**
	 * Moves the text cursor by a specified number of characters and clears the
	 * selection
	 */
	@Override
	public void moveCursorBy(int par1)
	{
		this.setCursorPosition(selectionEnd + par1);
	}

	/**
	 * sets the position of the cursor to the provided index
	 */
	@Override
	public void setCursorPosition(int par1)
	{
		cursorPosition = par1;
		int j = text.length();

		if (cursorPosition < 0)
		{
			cursorPosition = 0;
		}

		if (cursorPosition > j)
		{
			cursorPosition = j;
		}

		this.setSelectionPos(cursorPosition);
	}

	/**
	 * sets the cursors position to the beginning
	 */
	@Override
	public void setCursorPositionZero()
	{
		this.setCursorPosition(0);
	}

	/**
	 * sets the cursors position to after the text
	 */
	@Override
	public void setCursorPositionEnd()
	{
		this.setCursorPosition(text.length());
	}

	/**
	 * Call this method from you GuiScreen to process the keys into textbox.
	 */
	@Override
	public boolean textboxKeyTyped(char par1, int par2)
	{
		if (isEnabled && isFocused)
		{
			if (intsOnly && !Character.isDigit(par1) && par2 != 14 && par2 != 211 && par2 != 203 && par2 != 205)
				return false;

			switch (par1)
			{
				case 1:
					this.setCursorPositionEnd();
					this.setSelectionPos(0);
					return true;
				case 3:
					GuiScreen.setClipboardString(this.getSelectedtext());
					return true;
				case 22:
					this.writeText(GuiScreen.getClipboardString());
					return true;
				case 24:
					GuiScreen.setClipboardString(this.getSelectedtext());
					this.writeText("");
					return true;
				default:
					switch (par2)
					{
						case 14:
							if (GuiScreen.isCtrlKeyDown())
							{
								this.deleteWords(-1);
							}
							else
							{
								this.deleteFromCursor(-1);
							}

							return true;
						case 199:
							if (GuiScreen.isShiftKeyDown())
							{
								this.setSelectionPos(0);
							}
							else
							{
								this.setCursorPositionZero();
							}

							return true;
						case 203:
							if (GuiScreen.isShiftKeyDown())
							{
								if (GuiScreen.isCtrlKeyDown())
								{
									this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd()));
								}
								else
								{
									this.setSelectionPos(this.getSelectionEnd() - 1);
								}
							}
							else if (GuiScreen.isCtrlKeyDown())
							{
								this.setCursorPosition(this.getNthWordFromCursor(-1));
							}
							else
							{
								this.moveCursorBy(-1);
							}

							return true;
						case 205:
							if (GuiScreen.isShiftKeyDown())
							{
								if (GuiScreen.isCtrlKeyDown())
								{
									this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
								}
								else
								{
									this.setSelectionPos(this.getSelectionEnd() + 1);
								}
							}
							else if (GuiScreen.isCtrlKeyDown())
							{
								this.setCursorPosition(this.getNthWordFromCursor(1));
							}
							else
							{
								this.moveCursorBy(1);
							}

							return true;
						case 207:
							if (GuiScreen.isShiftKeyDown())
							{
								this.setSelectionPos(text.length());
							}
							else
							{
								this.setCursorPositionEnd();
							}

							return true;
						case 211:
							if (GuiScreen.isCtrlKeyDown())
							{
								this.deleteWords(1);
							}
							else
							{
								this.deleteFromCursor(1);
							}

							return true;
						default:
							if (ChatAllowedCharacters.isAllowedCharacter(par1))
							{
								this.writeText(Character.toString(par1));
								return true;
							}
							else
								return false;
					}
			}
		}
		else
			return false;
	}

	/**
	 * Args: x, y, buttonClicked
	 */
	@Override
	public void mouseClicked(int par1, int par2, int par3)
	{
		boolean flag = par1 >= xPos && par1 < xPos + width && par2 >= yPos && par2 < yPos + height;

		if (!CanFocus)
			return;

		if (flag && text == DummyText)
		{
			text = "";
		}

		if (canLoseFocus)
		{
			this.setFocused(isEnabled && flag);
		}

		if (isFocused && par3 == 0)
		{
			int l = par1 - xPos;

			if (enableBackgroundDrawing)
			{
				l -= 4;
			}

			String s = fontRenderer.trimStringToWidth(text.substring(lineScrollOffset), this.getWidth());
			this.setCursorPosition(fontRenderer.trimStringToWidth(s, l).length() + lineScrollOffset);
		}
	}

	/**
	 * Draws the textbox
	 */
	@Override
	public void drawTextBox()
	{
		if (this.getVisible())
		{
			int oldX = xPos, oldY = yPos;

			xPos = drawX;
			yPos = drawY;

			if (this.getEnableBackgroundDrawing())
			{
				drawRect(xPos - 1, yPos - 1, xPos + width + 1, yPos + height + 1, -6250336);
				drawRect(xPos, yPos, xPos + width, yPos + height, -16777216);
			}

			int i = isEnabled ? enabledColor : disabledColor;
			int j = cursorPosition - lineScrollOffset;
			int k = selectionEnd - lineScrollOffset;
			String s = fontRenderer.trimStringToWidth(text.substring(lineScrollOffset), this.getWidth());
			boolean flag = j >= 0 && j <= s.length();
			boolean flag1 = isFocused && cursorCounter / 6 % 2 == 0 && flag;
			int l = enableBackgroundDrawing ? xPos + 4 : xPos;
			int i1 = enableBackgroundDrawing ? yPos + (height - 8) / 2 : yPos;
			int j1 = l;

			if (k > s.length())
			{
				k = s.length();
			}

			if (s.length() > 0)
			{
				String s1 = flag ? s.substring(0, j) : s;
				j1 = fontRenderer.drawStringWithShadow(s1, l, i1, i);
			}

			boolean flag2 = cursorPosition < text.length() || text.length() >= this.getMaxStringLength();
			int k1 = j1;

			if (!flag)
			{
				k1 = j > 0 ? l + width : l;
			}
			else if (flag2)
			{
				k1 = j1 - 1;
				--j1;
			}

			if (s.length() > 0 && flag && j < s.length())
			{
				fontRenderer.drawStringWithShadow(s.substring(j), j1, i1, i);
			}

			if (flag1)
			{
				if (flag2)
				{
					Gui.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + fontRenderer.FONT_HEIGHT, -3092272);
				}
				else
				{
					fontRenderer.drawStringWithShadow("_", k1, i1, i);
				}
			}

			if (k != j)
			{
				int l1 = l + fontRenderer.getStringWidth(s.substring(0, k));
				this.drawCursorVertical(k1, i1 - 1, l1 - 1, i1 + 1 + fontRenderer.FONT_HEIGHT);
			}

			xPos = oldX;
			yPos = oldY;
		}
	}

	/**
	 * draws the vertical line cursor in the textbox
	 */
	private void drawCursorVertical(int par1, int par2, int par3, int par4)
	{
		int i1;

		if (par1 < par3)
		{
			i1 = par1;
			par1 = par3;
			par3 = i1;
		}

		if (par2 < par4)
		{
			i1 = par2;
			par2 = par4;
			par4 = i1;
		}

		Tessellator tessellator = Tessellator.instance;
		GL11.glColor4f(0.0F, 0.0F, 255.0F, 255.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_COLOR_LOGIC_OP);
		GL11.glLogicOp(GL11.GL_OR_REVERSE);
		tessellator.startDrawingQuads();
		tessellator.addVertex(par1, par4, 0.0D);
		tessellator.addVertex(par3, par4, 0.0D);
		tessellator.addVertex(par3, par2, 0.0D);
		tessellator.addVertex(par1, par2, 0.0D);
		tessellator.draw();
		GL11.glDisable(GL11.GL_COLOR_LOGIC_OP);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	@Override
	public void setMaxStringLength(int par1)
	{
		maxStringLength = par1;

		if (text.length() > par1)
		{
			text = text.substring(0, par1);
		}
	}

	/**
	 * returns the maximum number of character that can be contained in this
	 * textbox
	 */
	@Override
	public int getMaxStringLength()
	{
		return maxStringLength;
	}

	/**
	 * returns the current position of the cursor
	 */
	@Override
	public int getCursorPosition()
	{
		return cursorPosition;
	}

	/**
	 * get enable drawing background and outline
	 */
	@Override
	public boolean getEnableBackgroundDrawing()
	{
		return enableBackgroundDrawing;
	}

	/**
	 * enable drawing background and outline
	 */
	@Override
	public void setEnableBackgroundDrawing(boolean par1)
	{
		enableBackgroundDrawing = par1;
	}

	/**
	 * Sets the text colour for this textbox (disabled text will not use this
	 * colour)
	 */
	@Override
	public void setTextColor(int par1)
	{
		enabledColor = par1;
	}

	@Override
	public void func_82266_h(int par1)
	{
		disabledColor = par1;
	}

	/**
	 * setter for the focused field
	 */
	@Override
	public void setFocused(boolean par1)
	{
		if (par1 && !isFocused)
		{
			cursorCounter = 0;
		}

		isFocused = par1;
	}

	/**
	 * getter for the focused field
	 */
	@Override
	public boolean isFocused()
	{
		return isFocused;
	}

	@Override
	public void func_82265_c(boolean par1)
	{
		isEnabled = par1;
	}

	/**
	 * the side of the selection that is not the cursor, maye be the same as the
	 * cursor
	 */
	@Override
	public int getSelectionEnd()
	{
		return selectionEnd;
	}

	/**
	 * returns the width of the textbox depending on if the the box is enabled
	 */
	@Override
	public int getWidth()
	{
		return this.getEnableBackgroundDrawing() ? width - 8 : width;
	}

	/**
	 * Sets the position of the selection anchor (i.e. position the selection
	 * was started at)
	 */
	@Override
	public void setSelectionPos(int par1)
	{
		int j = text.length();

		if (par1 > j)
		{
			par1 = j;
		}

		if (par1 < 0)
		{
			par1 = 0;
		}

		selectionEnd = par1;

		if (fontRenderer != null)
		{
			if (lineScrollOffset > j)
			{
				lineScrollOffset = j;
			}

			int k = this.getWidth();
			String s = fontRenderer.trimStringToWidth(text.substring(lineScrollOffset), k);
			int l = s.length() + lineScrollOffset;

			if (par1 == lineScrollOffset)
			{
				lineScrollOffset -= fontRenderer.trimStringToWidth(text, k, true).length();
			}

			if (par1 > l)
			{
				lineScrollOffset += par1 - l;
			}
			else if (par1 <= lineScrollOffset)
			{
				lineScrollOffset -= lineScrollOffset - par1;
			}

			if (lineScrollOffset < 0)
			{
				lineScrollOffset = 0;
			}

			if (lineScrollOffset > j)
			{
				lineScrollOffset = j;
			}
		}
	}

	/**
	 * if true the textbox can lose focus by clicking elsewhere on the screen
	 */
	@Override
	public void setCanLoseFocus(boolean par1)
	{
		canLoseFocus = par1;
	}

	/**
	 * @return {@code true} if this textbox is visible
	 */
	@Override
	public boolean getVisible()
	{
		return visible;
	}

	/**
	 * Sets whether or not this textbox is visible
	 */
	@Override
	public void setVisible(boolean par1)
	{
		visible = par1;
	}
}