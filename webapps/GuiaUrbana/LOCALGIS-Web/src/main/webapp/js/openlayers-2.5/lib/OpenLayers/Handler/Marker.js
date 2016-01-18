OpenLayers.Handler.Marker = OpenLayers.Class.create();
	OpenLayers.Handler.Marker.prototype =
	  OpenLayers.Class.inherit(OpenLayers.Handler.Feature, 
	  {
		handle: function(evt) 
		{    
			var type = evt.type;
			var node = OpenLayers.Event.element(evt);
			var feature = null;
			for (var i = 0; i < this.layer.markers.length; i++) 
			{
				if (this.layer.markers[i].icon.imageDiv.firstChild == node) 
				{
					feature = this.layer.markers[i];
					break;
				}
			}
			var selected = false;
			if(feature) 
			{
				if(this.geometryTypes == null) 
				{	// over a new, out of the last and over a new, or still onthe last
					if(!this.feature) 
					{	// over a new feature
						this.callback('over', [feature]);
					} 
					else if(this.feature != feature) 
					{	// out of the last and over a new
						this.callback('out', [this.feature]);
						this.callback('over', [feature]);
					}
					this.feature = feature;
					this.callback(type, [feature]);
					selected = true;
				} 
				else 
				{
					if(this.feature && (this.feature != feature)) 
					{	// out of the last and over a new
						this.callback('out', [this.feature]);
						this.feature = null;
					}
					selected = false;
				}
			} 
			else 
			{
				if(this.feature) 
				{	// out of the last
					this.callback('out', [this.feature]);
					this.feature = null;
				}
				selected = false;
			}
			return selected;
		},
		CLASS_NAME: "OpenLayers.Handler.Marker"
	});