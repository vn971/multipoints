package com.google.sites.priymakpoints.pointsai.pointsAI_1_07;

public enum TemplateType implements Variables{
	
SQUARE_SIDE,SQUARE_CORNER,SQUARE,LONG,SHORT_SIDE,LONG_SIDE,WALL,ERROR,BLUE_SURROUND;

public String toString() {
	switch (this) {
		case SQUARE_SIDE:	return "sst";
		case SQUARE_CORNER:	return "sct";
		case SQUARE:		return "sqt";
		case SHORT_SIDE:	return "sht";
		case LONG:			return "lgt";
		case LONG_SIDE:		return "lst";
		case WALL:			return "wlt";
		case BLUE_SURROUND:	return "bst";
		case ERROR:			return "err";
		default:return "err";
	}
}

public boolean isSquare(){
	switch (this) {
		case SQUARE_SIDE:	return true;
		case SQUARE_CORNER:	return true;
		case SQUARE:		return true;
		case SHORT_SIDE:	return false;
		case LONG:			return false;
		case LONG_SIDE:		return false;
		case WALL:			return true;
		case BLUE_SURROUND:	return true;
		case ERROR:			return true;
		default:return true;
	}
}

public static TemplateType getTemplateType(String str){	
	if(str.equals(TemplateType.SQUARE_SIDE.toString()))return TemplateType.SQUARE_SIDE;
	if(str.equals(TemplateType.SQUARE_CORNER.toString()))return TemplateType.SQUARE_CORNER;
	if(str.equals(TemplateType.SQUARE.toString()))return TemplateType.SQUARE;
	if(str.equals(TemplateType.SHORT_SIDE.toString()))return TemplateType.SHORT_SIDE;
	if(str.equals(TemplateType.LONG.toString()))return TemplateType.LONG;
	if(str.equals(TemplateType.LONG_SIDE.toString()))return TemplateType.LONG_SIDE;
	if(str.equals(TemplateType.WALL.toString()))return TemplateType.WALL;
	if(str.equals(TemplateType.BLUE_SURROUND.toString()))return TemplateType.BLUE_SURROUND;
	return TemplateType.ERROR;
}

public String getContent(PointsAIGame game,int x,int y,boolean isVertical){try{
	String content="";
	String[][] fieldState=game.getFieldState();
	
	switch (this) {
		case SQUARE_SIDE:	
			if(x>(sizeX-6)&y>5&y<(sizeY-5)){
				for(int j=y-5;j<y+4;j++){for(int i=31;i<39;i++)content+=fieldState[i][j];content+="LOOOO";}
			}
			if(x<6&y>5&y<(sizeY-5)){
				for(int j=y-5;j<y+4;j++){content+="L";for(int i=0;i<8;i++)content+=fieldState[i][j];content+="OOOO";}
			}
			if(y>(sizeY-6)&x>5&x<(sizeX-5)){
				for(int j=(sizeY-8);j<sizeY;j++){for(int i=x-5;i<x+4;i++)content+=fieldState[i][j];content+="OOOO";}content+="LLLLLLLLLOOOO";
			}
			if(y<6&x>5&x<(sizeX-5)){
				content+="LLLLLLLLLOOOO";for(int j=0;j<8;j++){for(int i=x-5;i<x+4;i++)content+=fieldState[i][j];content+="OOOO";}
			}
			return content;			
		
		case SQUARE_CORNER:	
				if(x<6&y<6){content+="LLLLLLLLLOOOO";for(int j=0;j<8;j++){content+="L";for(int i=0;i<8;i++)content+=fieldState[i][j];content+="OOOO";}}
				if(x<6&y>(sizeY-6)){for(int j=(sizeY-8);j<sizeY;j++){content+="L";for(int i=0;i<8;i++)content+=fieldState[i][j];content+="OOOO";}content+="LLLLLLLLLOOOO";}
				if(x>(sizeX-6)&y>(sizeY-6)){for(int j=(sizeY-8);j<sizeY;j++){for(int i=(sizeX-8);i<sizeX;i++)content+=fieldState[i][j];content+="LOOOO";}content+="LLLLLLLLLOOOO";}
				if(x>(sizeX-6)&y<6){content+="LLLLLLLLLOOOO";for(int j=0;j<8;j++){for(int i=(sizeX-8);i<sizeX;i++)content+=fieldState[i][j];content+="LOOOO";}}
				return content;
				
		case SHORT_SIDE:	
			if(x>(sizeX-13)&y>5&y<(sizeY-5)){
				for(int j=y-5;j<y+4;j++){for(int i=(sizeX-12);i<sizeX;i++)content+=fieldState[i][j];content+="L";}
			}
			if(x<11&y>5&y<(sizeY-5)){
				for(int j=y-5;j<y+4;j++){content+="L";for(int i=0;i<12;i++)content+=fieldState[i][j];}
			}
			if(y>(sizeY-10)&x>5&x<(sizeX-5)){
				for(int i=x-5;i<x+4;i++){for(int j=(sizeY-12);j<sizeY;j++)content+=fieldState[i][j];content+="L";}
			}
			if(y<11&x>5&x<(sizeX-5)){
				for(int i=x-5;i<x+4;i++){content+="L";for(int j=0;j<12;j++)content+=fieldState[i][j];}
			}
			return content;			
		
		case LONG:
			if(!isVertical){
				for(int j=0;j<9;j++){
					for(int i=0;i<13;i++){
						if((i+game.getLastX()-7<0)|(j+game.getLastY()-5<0)|(i+game.getLastX()-7>(sizeX-1))|(j+game.getLastY()-5>(sizeY-1)))content+="N";
						else content+=fieldState[i+game.getLastX()-7][j+game.getLastY()-5];}
				}
			}else{
				for(int i=0;i<9;i++){
					for(int j=0;j<13;j++){
						if((i+game.getLastX()-5<0)|(j+game.getLastY()-7<0)|(i+game.getLastX()-5>(sizeX-1))|(j+game.getLastY()-7>(sizeY-1)))content+="N";
						else content+=fieldState[i+game.getLastX()-5][j+game.getLastY()-7];}
				}
			}
			return content;
		
		case LONG_SIDE:		
			if(x>(sizeX-7)&y>7&y<(sizeY-7)){
				for(int i=(sizeX-8);i<sizeX;i++){for(int j=y-7;j<y+6;j++)content+=fieldState[i][j];}content+="LLLLLLLLLLLLL";
			}
			if(x<6&y>7&y<(sizeY-7)){
				content+="LLLLLLLLLLLLL";for(int i=0;i<8;i++){for(int j=y-7;j<y+6;j++)content+=fieldState[i][j];}
			}
			if(y>(sizeY-6)&x>7&x<sizeY){
				for(int j=(sizeY-8);j<sizeY;j++){for(int i=x-7;i<x+6;i++)content+=fieldState[i][j];}content+="LLLLLLLLLLLLL";
			}
			if(y<6&x>7&x<sizeY){
				content+="LLLLLLLLLLLLL";for(int j=0;j<8;j++){for(int i=x-7;i<x+6;i++)content+=fieldState[i][j];}
			}
			return content;			
		
		case WALL: case SQUARE:	case BLUE_SURROUND:
			for(int j=0;j<9;j++){
				for(int i=0;i<9;i++){
					if((i+x-5<0)|(j+y-5<0)|(i+x-5>(sizeX-1))|(j+y-5>(sizeY-1)))content+="N";
					else content+=fieldState[i+x-5][j+y-5];}
				content+="OOOO";
			}
			return content;
			
		case ERROR:			return null;
		
		default:return null;
	}}catch(Exception e){return null;}
}
	
}
