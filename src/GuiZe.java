
public class GuiZe
{
	QiZi[][] qiZi;//�������ӵ�����
	boolean canMove=false;
	int i;
	int j;
	public GuiZe(QiZi[][] qiZi)
	{
		this.qiZi=qiZi;
	}
	public boolean canMove(int startI,int startJ,int endI,int endJ,String name)
	{
		int maxI;//����һЩ��������
		int minI;
		int maxJ;
		int minJ;
		canMove=true;
		if(startI>=endI)//ȷ����ʵ����Ĵ�С��ϵ
		{
			maxI=startI;
			minI=endI;
		}
		else//ȷ��maxI��minI
		{
			maxI=endI;
			minI=startI;
		}
		if(startJ>=endJ)//ȷ��endJ��startJ
		{
			maxJ=startJ;
			minJ=endJ;
		}
		else
		{
			maxJ=endJ;
			minJ=startJ;
		}
		if(name.equals("܇"))//�����"܇"
		{
			this.ju(maxI,minI,maxJ,minJ);
		}
		else if(name.equals("�R"))//�����"�R"
		{
			this.ma(maxI,minI,maxJ,minJ,startI,startJ,endI,endJ);
		}
		else if(name.equals("��"))//�����"��"
		{
			this.xiang1(maxI,minI,maxJ,minJ,startI,startJ,endI,endJ);
		}
		else if(name.equals("��"))//�����"��"
		{
			this.xiang2(maxI,minI,maxJ,minJ,startI,startJ,endI,endJ);
		}
		else if(name.equals("ʿ")||name.equals("��")) //�����"ʿ"
		{
			this.shi(maxI,minI,maxJ,minJ,startI,startJ,endI,endJ);
		}
		else if(name.equals("��")||name.equals("��"))//�����"��"
		{
			this.jiang(maxI,minI,maxJ,minJ,startI,startJ,endI,endJ);
		}
		else if(name.equals("��")||name.equals("�h"))//�����"��"
		{
			this.pao(maxI,minI,maxJ,minJ,startI,startJ,endI,endJ);
		}
		else if(name.equals("��"))//�����"��"
		{
			this.bing(maxI,minI,maxJ,minJ,startI,startJ,endI,endJ);
			
		}
		else if(name.equals("��"))//�����"��"
		{
			this.zu(maxI,minI,maxJ,minJ,startI,startJ,endI,endJ);
		}
		return canMove;
	}
	public void ju(int maxI,int minI,int maxJ,int minJ)
	{//��"܇"�Ĵ�����
		if(maxI==minI)//�����һ��������
		{
			for(j=minJ+1;j<maxJ;j++)
			{
				if(qiZi[maxI][j]!=null)//����м�������
				{
					canMove=false;//����������
					break;
				}
			}
		}
		else if(maxJ==minJ)//�����һ��������
		{
			for(i=minJ+1;i<maxJ;i++)
			{
				if(qiZi[i][maxJ]!=null)//����м�������
				{
					canMove=false;//����������
					break;
				}
			}
		}
		else if(maxI!=minI&&maxJ!=minJ)//�������ͬһ������
		{
			canMove=false;//����������
		}
	}
	public void ma(int maxI,int minI,int maxJ,int minJ,int startI,int startJ,int endI,int endJ)
	{//��"�R"�Ĵ�����
		int a=maxI-minI;
		int b=maxJ-minJ;//����������֮��Ĳ�
		if(a==1&&b==2)//��������ŵ�"��"��
		{
			if(startJ>endJ)//����Ǵ���������
			{
				if(qiZi[startI][startJ-1]!=null)//������ȴ�������
				{
					canMove=false;//��������
				}	
			}
			else
			{//����Ǵ���������
				if(qiZi[startI][startJ+1]!=null)//������ȴ�������
				{
					canMove=false;//��������
				}
			}
		}
		else if(a==2&&b==1)//����Ǻ��ŵ�"��"
		{
			if(startI>endI)//����Ǵ���������
			{
				if(qiZi[startI-1][startJ]!=null)//������ȴ�������
				{
					canMove=false;//��������
				}	
			}
			else
			{//����Ǵ���������
				if(qiZi[startI+1][startJ]!=null)//������ȴ�������
				{
					canMove=false;//��������
				}
			}
		}
		else if(!((a==2&&b==1)||(a==1&&b==2)))//�����ʱ"��"��
		{
			canMove=false;//��������
		}
	}
	public void xiang1(int maxI,int minI,int maxJ,int minJ,int startI,int startJ,int endI,int endJ)
	{//��"��"�Ĵ���
		int a=maxI-minI;
		int b=maxJ-minJ;//���X,Y����Ĳ�
		if(a==2&&b==2)//�����"��"��
		{
			if(endJ>4)//��������� 
			{
				canMove=false;//��������
			}
			if(qiZi[(maxI+minI)/2][(maxJ+minJ)/2]!=null)//���"��"���м�������
			{
				canMove=false;//��������
			}
		}
		else
		{
			canMove=false;//�������"��"�֣���������
		}
	}
	public void xiang2(int maxI,int minI,int maxJ,int minJ,int startI,int startJ,int endI,int endJ)
	{//��"��"�Ĵ���
		int a=maxI-minI;
		int b=maxJ-minJ;//���X,Y����Ĳ�
		if(a==2&&b==2)//�����"��"��
		{
			if(endJ<5)//��������� 
			{
				canMove=false;//��������
			}
			if(qiZi[(maxI+minI)/2][(maxJ+minJ)/2]!=null)//���"��"���м�������
			{
				canMove=false;//��������
			}
		}
		else
		{
			canMove=false;//�������"��"�֣���������
		}
	}
	public void shi(int maxI,int minI,int maxJ,int minJ,int startI,int startJ,int endI,int endJ)
	{
		int a=maxI-minI;
		int b=maxJ-minJ;//���X,Y����Ĳ�
		if(a==1&&b==1)//�����Сб��
		{
			if(startJ>4)//������·���ʿ
			{
				if(endJ<7)
				{
					canMove=false;//����·���ʿԽ�磬��������
				}
			}
			else
			{//������Ϸ�����
				if(endJ>2)
				{
					canMove=false;//����Ϸ�����Խ�磬��������
				}
			}
			if(endI>5||endI<3)//�������Խ�磬�򲻿�����
			{
				canMove=false;
			}
		}
		else
		{//�����ʱСб��
			canMove=false;//��������
		}
	}
	public void jiang(int maxI,int minI,int maxJ,int minJ,int startI,int startJ,int endI,int endJ)
	{//��"��"��"��"�Ĵ���
		int a=maxI-minI;
		int b=maxJ-minJ;//���X,Y����Ĳ�
		if((a==1&&b==0)||(a==0&&b==1))
		{//����ߵ���һС��
			if(startJ>4)//������·��Ľ�
			{
				if(endJ<7)//���Խ��
				{
					canMove=false;//��������
				}
			}
			else
			{//������Ϸ��Ľ�
				if(endJ>2)//���Խ��
				{
					canMove=false;//��������
				}
			}
			if(endI>5||endI<3)//�������Խ�磬��������
			{
				canMove=false;
			}
		}
		else
		{//����ߵĲ���һ�񣬲�������
			canMove=false;
		}
	}
	public void pao(int maxI,int minI,int maxJ,int minJ,int startI,int startJ,int endI,int endJ)
	{//��"��"��"�h"�Ĵ���
		if(maxI==minI)//����ߵ�����
		{
			if(qiZi[endI][endJ]!=null)//����յ�������
			{
				int count=0;
				for(j=minJ+1;j<maxJ;j++)
				{
					if(qiZi[minI][j]!=null)//�ж�������յ�֮���м�������
					{
						count++;
					}
				}
				if(count!=1)
				{//�������һ������
					canMove=false;//��������
				}
			}
			else if(qiZi[endI][endJ]==null)//����յ�û������
			{
				for(j=minJ+1;j<maxJ;j++)
				{
					if(qiZi[minI][j]!=null)//�����ֹ��֮��������
					{
						canMove=false;//��������
						break;
					}
				}
			}
		}
		else if(maxJ==minJ)//����ߵ��Ǻ���
		{
			if(qiZi[endI][endJ]!=null)//����յ�������
			{
				int count=0;
				for(i=minI+1;i<maxI;i++)
				{
					if(qiZi[i][minJ]!=null)//�ж�������յ�֮���м�������
					{
						count++;
					}
				}
				if(count!=1)//�������һ������
				{
					canMove=false;//��������
				}
			}
			else if(qiZi[endI][endJ]==null)//����յ�û������
			{
				for(i=minI+1;i<maxI;i++)
				{
					if(qiZi[i][minJ]!=null)//�����ֹ��֮��������
					{
						canMove=false;//��������
						break;
					}
				}
			}
		}
		else if(maxJ!=minJ&&maxI!=minI)
		{//����ߵļȲ������ߣ�Ҳ���Ǻ��ߣ��򲻿�����
			canMove=false;
		}
	}
	public void bing(int maxI,int minI,int maxJ,int minJ,int startI,int startJ,int endI,int endJ)
	{//��"��"�Ĵ���
		if(startJ<5)//�����û�й���
		{
			if(startI!=endI)//���������ǰ��
			{
				canMove=false;//��������
			}
			if(endJ-startJ!=1)//����ߵĲ���һ��
			{
				canMove=false;//��������
			}
		}
		else
		{//����Ѿ�����
			if(startI==endI)
			{//����ߵ�������
				if(endJ-startJ!=1)//����ߵĲ���һ��
				{
					canMove=false;//��������
				}
			}
			else if(startJ==endJ)
			{//����ߵ��Ǻ���
				if(maxI-minI!=1)
				{//����ߵĲ���һ��
					canMove=false;//��������
				}
			}
			else if(startI!=endI&&startJ!=endJ)
			{//����ߵļȲ������ߣ�Ҳ���Ǻ��ߣ��򲻿�����
				canMove=false;
			}
		}
	}
	public void zu(int maxI,int minI,int maxJ,int minJ,int startI,int startJ,int endI,int endJ)
	{//��"��"�Ĵ���
		if(startJ>4)
		{//�����û�й���
			if(startI!=endI)
			{//���������ǰ��
				canMove=false;//��������
			}
			if(endJ-startJ!=-1)//����ߵĲ���һ��
			{
				canMove=false;
			}
		}
		else
		{//����Ѿ�����
			if(startI==endI)
			{//����ߵ�������
				if(endJ-startJ!=-1)
				{//����ߵĲ���һ��
					canMove=false;//��������
				}
			}
			else if(startJ==endJ)
			{//����ߵ��Ǻ���
				if(maxI-minI!=1)
				{//����ߵĲ���һ��
					canMove=false;//��������
				}
			}
			else if(startI!=endI&&startJ!=endJ)
			{//����ߵļȲ������ߣ�Ҳ���Ǻ��ߣ��򲻿�����
				canMove=false;
			}
		}
	}
}
