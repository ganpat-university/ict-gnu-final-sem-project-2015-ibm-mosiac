from django.shortcuts import render
from django.shortcuts import render, redirect
from django.http import HttpResponse
from django.template.response import TemplateResponse
from django.shortcuts import get_object_or_404
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from . models import *
from . serializers import *
from . haversineformula import haversine
#API function to display all restaurants //COMPLETED
class restaurantList(APIView):

	def get(self,request):
		restaurants=restaurant.objects.all()
		serializer=restaurantSerializer(restaurants,many=True)
		return Response(serializer.data) 

#API function to resturn nearby restaurants information //Working		
class restaurantNearBy(APIView):
	def get(self,request):
		distancelist=[]
		restlocinfo=list(restaurant.objects.values('id','restaurant_name','longitude','latitude'))
		for i in range(0,len(restlocinfo)):
			longitude=float(restlocinfo[i].get('longitude'))
			latitude=float(restlocinfo[i].get('latitude'))
			dist=haversine(latitude,longitude)
			if(dist<5):
				rest_id=restlocinfo[i].get('id')
				rest_name=restlocinfo[i].get('restaurant_name')
				distancedict={'id':rest_id, 'restaurant_name':rest_name, 'distance':dist}
				distancelist.append(distancedict)
		return Response(distancelist)

#API function to display employees details
class employeeList(APIView):
	def get(self,request):
		employees=employee.objects.all()
		serializer=employeeSerializer(employees,many=True)
		return Response(serializer.data) 

class business_opportunityList(APIView):
	def get(self,request):
		businessopportunity=business_opportunity.objects.all()
		serializer=business_opportunitySerializer(businessopportunity,many=True)
		return Response(serializer.data) 

class meetingList(APIView):
	def get(self,request):
		meet=meeting.objects.all()
		serializer=meetingSerializer(meet,many=True)
		return Response(serializer.data) 

	def post(self,request):
		restaurant_id=request.data.get('restaurant_id')
		#business_opportunity=request.data.get('business_opportunity')
		#employee_id=request.data.get('employee_id')
		#contacted_person=request.data.get('contacted_person')
		#meeting_date=request.data.get('meeting_date')
		#meeting_description=request.data.get('meeting_description')
		print(restaurant_id)
		return Response({'rest_id':restaurant_id})


'''
#Normal function for website to display restaurants id
def restaurantList(request):
	context=[]
	context=list(restaurant.objects.values('id'))
	print(context[0].get('id'))
	return render(request,"restaurantsdisplay.html")

#API Function to display restaurants id
class restaurantList(APIView):

	def get(self,request):
		context=[]
		context=list(restaurant.objects.values('id'))
		serializer=restaurantSerializer(context,many=True)
		return Response(context)  
'''
