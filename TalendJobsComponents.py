import os
import csv
import xml.etree.ElementTree as ET

def extract_jobs_and_components(project_path):
    jobs = []

    # Traverse the project directory
    for root, dirs, files in os.walk(project_path):
        for file in files:
            if file.endswith('.item'):
                file_path = os.path.join(root, file)
                tree = ET.parse(file_path)
                root_element = tree.getroot()
                
                job_name = root_element.attrib.get('label', 'Unknown')
                components = []

                for element in root_element.iter('node'):
                    component_name = element.attrib.get('componentName', 'Unknown')
                    components.append(component_name)

                jobs.append({
                    'job_name': job_name,
                    'components': components
                })

    return jobs

def export_to_csv(jobs, output_file):
    with open(output_file, mode='w', newline='') as file:
        writer = csv.writer(file)
        writer.writerow(['Job Name', 'Component Names'])

        for job in jobs:
            writer.writerow([job['job_name'], ', '.join(job['components'])])

if __name__ == "__main__":
    project_path = 'path/to/your/talend/project'  # Change this to the path of your Talend project
    output_file = 'talend_jobs_components.csv'

    jobs = extract_jobs_and_components(project_path)
    export_to_csv(jobs, output_file)

    print(f"Extraction complete. Data saved to {output_file}")
