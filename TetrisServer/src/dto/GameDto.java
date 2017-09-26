package dto;

import java.io.Serializable;
import java.util.Arrays;

import entity.GameAct;

/**
 * ���ݴ������
 * @author Administrator
 *
 */
public class GameDto implements Serializable{



	@Override
	public String toString() {
		return "GameDto [isEnd=" + isEnd + ", result=" + result + ", sleepTime=" + sleepTime + ", td=" + td
				+ ", gameMap=" + Arrays.toString(gameMap) + ", gameAct=" + gameAct + ", next=" + next + ", nowlevel="
				+ nowlevel + ", beforePoints=" + beforePoints + ", nextPoints=" + nextPoints + ", upPoints=" + upPoints
				+ ", nowPoint=" + nowPoint + ", nowRemoveLine=" + nowRemoveLine + "]";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//1.��Ϸgameover 0.��Ϸ������   2.��Ϸδ��ʼ 3.��ͣ״̬ 
	private volatile Integer isEnd = 2;
	
	//������� 0.�޽��  1.���� 2.Ӯ��
	private volatile Integer result = 0;
	
	private volatile Integer sleepTime = 1000;
	
	private volatile TipDto td = new TipDto();
	
	private volatile MapCell[][] gameMap;
	
	public GameDto(){
		
		initGameMap();
		
	}
	
	/**
	 * ��ʼ����Ϸ��ͼ
	 */
	private void initGameMap() {
		
		gameMap = new MapCell[18][10];
		
		for (int i = 0; i< gameMap.length;i++) {
			for(int j =0;j < gameMap[i].length;j++){
				gameMap[i][j] = new MapCell();
				}
			}
		
	}

	private volatile GameAct gameAct;
	
	private volatile Integer next = 0;
	
	private volatile Integer nowlevel = 1;
	
	//��һ�ȼ���������Ҫ�Ļ���
	private volatile Integer beforePoints = 0;
	
	//��һ��
	private volatile Integer nextPoints = 0;
	
	//��ǰ�ȼ���������Ҫ�Ļ���
	private volatile Integer upPoints = 1200;
			
	private volatile Integer nowPoint = 0;
	
	private volatile Integer nowRemoveLine = 0;

	public synchronized void addPoints(Integer add){
		nowPoint += add;
	}
	
	public synchronized void removeLine(){
		nowRemoveLine ++;
	}
	
	public synchronized void levelUp(){
		nowlevel ++;
		upPoints = nowlevel* 200 + 1000;
		if(nowlevel%10 == 0){
			if(sleepTime > 100)this.sleepTime -=100; 
			System.out.println("sleepTime===========>"+sleepTime);
		}
	}
	

	public synchronized MapCell[][] getGameMap() {
		return gameMap;
	}

	public synchronized void setGameMap(MapCell[][] gameMap) {
		this.gameMap = gameMap;
	}

	public GameAct getGameAct() {
		return gameAct;
	}

	public void setGameAct(GameAct gameAct) {
		this.gameAct = gameAct;
	}

	public Integer getNext() {
		return next;
	}

	public void setNext(Integer next) {
		this.next = next;
	}


	public int getNowPoint() {
		return nowPoint;
	}

	public void setNowPoint(Integer nowPoint) {
		this.nowPoint = nowPoint;
	}

	public int getNowRemoveLine() {
		return nowRemoveLine;
	}

	public void setNowRemoveLine(Integer nowRemoveLine) {
		this.nowRemoveLine = nowRemoveLine;
	}

	public int getNowlevel() {
		return nowlevel;
	}

	public void setNowlevel(Integer nowlevel) {
		this.nowlevel = nowlevel;
	}

	public int getIsEnd() {
		return isEnd;
	}

	public void setIsEnd(Integer isEnd) {
		this.isEnd = isEnd;
	}

	public int getUpPoints() {
		return upPoints;
	}

	public void setUpPoints(Integer upPoints) {
		this.upPoints = upPoints;
	}

	public int getBeforePoints() {
		return 100*(nowlevel-1)*(nowlevel-1)+1100*(nowlevel-1);
	}

	public void setBeforePoints(Integer beforePoints) {
		this.beforePoints = beforePoints;
	}

	public TipDto getTd() {
		return td;
	}

	public void setTd(TipDto td) {
		this.td = td;
	}

	public int getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(Integer sleepTime) {
		this.sleepTime = sleepTime;
	}

	public int getNextPoints() {
		return 100*nowlevel*nowlevel+1100*nowlevel;
	}

	public void setNextPoints(Integer nextPoints) {
		this.nextPoints = nextPoints;
	}

	/**
	 * ��������~
	 */
	public void reSet() {
		this.initGameMap();
		setBeforePoints(0);
		setUpPoints(1200);
		setNowlevel(1);
		setNowPoint(0);
		setNowRemoveLine(0);
        setIsEnd(0);		
        setResult(0);
	}
	
	public void superReSet() {
		this.initGameMap();
		setBeforePoints(0);
		setUpPoints(1200);
		setNowlevel(1);
		setNowPoint(0);
		setNowRemoveLine(0);
        setIsEnd(2);		
        setResult(0);
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}
	
}
