package Controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.face.Face;
import org.opencv.face.FaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RecController implements Initializable{
	 
	    @FXML
	    private JFXButton recBtn;

	    @FXML
	    private JFXCheckBox haar;

	    @FXML
	    private JFXCheckBox lbp;

	    @FXML
	    private ImageView frame;
	    
	    
	    private VideoCapture capture;
		private CascadeClassifier faceCascade;
		private ScheduledExecutorService timer;
		
		private boolean cameraActive;
		private int absoluteFaceSize;
	
		public HashMap<Integer, String> names = new HashMap<Integer, String>();
		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			
			this.faceCascade =new CascadeClassifier();
			this.capture=new VideoCapture();
			this.absoluteFaceSize=0;
			this.cameraActive=false;
		
			trainModel();

		}
		
		private void trainModel() {
			System.out.println("Entered train");
			
			File root = new File("resources/trainingset/");
			
			
			FilenameFilter imgFilter = new FilenameFilter() {
	            public boolean accept(File dir, String name) {
	                name = name.toLowerCase();
	                return name.endsWith(".png");
	            }
	        };
	        
	        File[] imageFiles = root.listFiles(imgFilter);
	        
	        List<Mat> images = new ArrayList<Mat>();
	        
	        System.out.println("THE NUMBER OF IMAGES READ IS: " + imageFiles.length);
	        
	        
	        Mat labels = new Mat(imageFiles.length,1,CvType.CV_32SC1);
	        
	        int counter = 0;
	        
	        for (File image : imageFiles) {
	        	Mat img = Imgcodecs.imread(image.getAbsolutePath());
	        	Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);
	        	Imgproc.equalizeHist(img, img);
	        	
	        	int label = Integer.parseInt(image.getName().split("\\-")[0]);
	        	
	        	String labname = image.getName().split("\\_")[0];
	        	String name = labname.split("\\-")[1];
	        	names.put(label, name);
	        	
	        	images.add(img);

	        	labels.put(counter, 0, label);
	        	counter++;
	        } 
            FaceRecognizer faceRecognizer = Face.createLBPHFaceRecognizer();
            
            faceRecognizer.train(images, labels);
            faceRecognizer.save("traineddata");
			
		}
		@FXML
	    protected void startCam(ActionEvent event) 
		{
			this.haar.setDisable(true);
			this.lbp.setDisable(true);
			if(!this.cameraActive) {
				this.capture.open(0);
				if(this.capture.isOpened()) 
				{
					this.cameraActive=true;
					
					Runnable frameGrabber= new Runnable() {
						@Override
						public void run() 
						{
							//System.out.println("Working");
							Image imageToShow=grabFrame();
	     					frame.setImage(imageToShow);	
						} 
						
						
					};
					this.timer=Executors.newSingleThreadScheduledExecutor();
					this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
					this.recBtn.setText("Stop Camera");
				}
				else {
					System.err.println("Can't open cam ");
				}
			}
			else 
			{
				this.cameraActive=false;
				this.haar.setDisable(false);
				this.lbp.setDisable(false);
				this.recBtn.setDisable(false);
				try {
						this.timer.shutdown();
						this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
				}
				catch(Exception e)
				{
					System.out.println("Problem in shutting camera "+e);
				}
				this.capture.release();
				this.frame.setImage(null);
				this.recBtn.setText("Recognize");
			}
		

	    }
		protected Image grabFrame() {
			
			Image imageToShow=null;
			Mat mat=new Mat();
			this.capture.read(mat);
			if(mat!=null) {
					
					this.rec(mat);
		
				imageToShow=mat2Img(mat);
			}
			return imageToShow;
		}
		private Image mat2Img(Mat mat) {
			MatOfByte buff=new MatOfByte();
			Imgcodecs.imencode(".png", mat, buff);
			return new Image(new ByteArrayInputStream(buff.toArray()));
			
		}


	   
	    protected void rec(Mat mat) {
	    	MatOfRect faces = new MatOfRect();
			Mat grayFrame = new Mat();
			
			Imgproc.cvtColor(mat, grayFrame, Imgproc.COLOR_BGR2GRAY);
			Imgproc.equalizeHist(grayFrame, grayFrame);
			
			if (this.absoluteFaceSize == 0)
			{
				int height = grayFrame.rows();
				if (Math.round(height * 0.2f) > 0)
				{
					this.absoluteFaceSize = Math.round(height * 0.2f);
				}
			}

			this.faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
					new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size());
					
			Rect[] facesArray = faces.toArray(); 
			for (int i = 0; i < facesArray.length; i++) {
				Imgproc.rectangle(mat, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0), 3);

				Rect rectCrop = new Rect(facesArray[i].tl(), facesArray[i].br());
				Mat croppedImage = new Mat(mat, rectCrop);
				Imgproc.cvtColor(croppedImage, croppedImage, Imgproc.COLOR_BGR2GRAY);
				Imgproc.equalizeHist(croppedImage, croppedImage);
				Mat resizeImage = new Mat();
				Size size = new Size(250,250);
				Imgproc.resize(croppedImage, resizeImage, size);


	    	// predict the label
	    	int[] predLabel = new int[1];
	        double[] confidence = new double[1];
	        int result = -1;
	        
	        FaceRecognizer faceRecognizer = Face.createLBPHFaceRecognizer();
	        faceRecognizer.load("traineddata");
	    	faceRecognizer.predict(resizeImage,predLabel,confidence);
	    	result = predLabel[0];
	    	String name = "";
	    	if (names.containsKey(result)) {
				name = names.get(result);
			} else {
				name = "Unknown";
			}
			
	        String box_text = "Prediction = " + name + " Confidence = " + confidence[0];
	        double pos_x = Math.max(facesArray[i].tl().x - 10, 0);
	        double pos_y = Math.max(facesArray[i].tl().y - 10, 0);
	        Imgproc.putText(mat, box_text, new Point(pos_x, pos_y), 
	        		Core.FONT_HERSHEY_PLAIN, 1.0, new Scalar(0, 255, 0, 2.0));
		}
				


	    }

		 @FXML
		   protected void haarSelector(ActionEvent event) 
		 	{
			 
			 	if(this.lbp.isSelected()) 
			 		this.lbp.setSelected(false);
			 		
			 	Selector("resources/haarcascades/haarcascade_frontalface_alt.xml");
		    }

			@FXML
		   protected void lbpSelector(ActionEvent event) {
		    	if(this.haar.isSelected()) 
			 		this.haar.setSelected(false);
		    	Selector("resources/lbpcascades/lbpcascade_frontalface.xml");	

		    }

		    private void Selector(String path) {
		    	this.faceCascade.load(path);
		    	this.recBtn.setDisable(false);
			
		}
		

}
