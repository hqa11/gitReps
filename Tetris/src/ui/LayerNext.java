package ui;

import java.awt.Graphics;
import java.awt.Point;

import config.Constant;
import dto.GameDto;
import entity.GameAct;

public class LayerNext extends Layer{

	public LayerNext(int x, int y, int w, int h) {
		super(x, y, w, h);
	}

	public void paint(Graphics g){
		
		this.createWindow(g);
		GameDto gameDto = this.getGameDto();
		int code = gameDto.getNext();
		Point[] points = GameAct.TYPE_CONFIG.get(code);
		//��ȡ����λ��
				int sx =  (code+1)*32;
				int ex =  (code+2)*32;
				int sy =  0;
				int ey =  32;
				//����һ��ƫ����
				int offsetX = 0;
				int offsetY = 0;
				//�����λ����
				int p = 3;
				if(code==0){
					offsetX = (this.w -142)/2;
					offsetY = (this.h -46)/2;
				}else if(code == 1){
					offsetX = (this.w -110)/2;
					offsetY = (this.h -78)/2;
				}else if(code == 3 || code == 6){
					offsetX = (this.w -110)/2;
					offsetY = (this.h -78)/2;
				}else if(code == 2 || code == 5){
					offsetX = (this.w -110)/2;
					offsetY = (this.h -78)/2;
				}else if(code == 4){
					offsetX = (this.w -78)/2;
					offsetY = (this.h -78)/2;
					p = 4;
				}
				
				offsetY = offsetY  + Constant.REFRESHOFFSETY * 32;
				
				//��ʼ������
				for (Point point : points) {
					
					//��ʼ����ɫ����
					g.drawImage(Constant.UI_CONSTANT.GAME_IMG,
							this.x + (point.x-p) * 32 +7 + offsetX,
							this.y + point.y * 32 +7 + offsetY,
							this.x + (point.x-p) * 32 +39 + offsetX,
							this.y + point.y * 32 +39+ offsetY,
							sx, sy,ex ,ey, null);
				}
	}
	
}
