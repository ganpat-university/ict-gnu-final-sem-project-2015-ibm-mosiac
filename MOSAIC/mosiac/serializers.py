from rest_framework import serializers
from .models import *

class restaurantSerializer(serializers.ModelSerializer):
	class Meta:
		model=restaurant
		fields='__all__'

class employeeSerializer(serializers.ModelSerializer):
	class Meta:
		model=employee
		fields='__all__'
		
	def create(self, validated_data):
		return employee.objects.create(**validated_data)


class business_opportunitySerializer(serializers.ModelSerializer):
	class Meta:
		model=business_opportunity
		fields='__all__'

class meetingSerializer(serializers.ModelSerializer):
	class Meta:
		model=meeting
		fields='__all__'

	def create(self, validated_data):
		return meeting.objects.create(**validated_data)
